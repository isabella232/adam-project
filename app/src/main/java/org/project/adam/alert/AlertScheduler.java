package org.project.adam.alert;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.project.adam.AppDatabase;
import org.project.adam.Preferences_;
import org.project.adam.persistence.Lunch;
import org.project.adam.util.DateFormatters;

import java.util.Calendar;
import java.util.List;

import timber.log.Timber;

import static org.project.adam.alert.AlertReceiver.RECEIVER_ACTION;


@EBean
public class AlertScheduler {

    @SystemService
    protected AlarmManager alarmMgr;

    @RootContext
    protected Context context;

    @Pref
    Preferences_ preferences;

    AppDatabase appDatabase;

    @AfterInject
    void setUpDatabase() {
        appDatabase = AppDatabase.getDatabase(context);
    }

    public void schedule() {

        cancelAllAlarms();

        int dietId = preferences.currentDietId().getOr(-1);
        if (dietId == -1) {
            return;
        }

        setupAlarms(dietId);
    }

    @Background
    void setupAlarms(int dietId) {

        Timber.d("Setting up alarms");

        List<Lunch> lunches = appDatabase.lunchDao().findFromDietSync(dietId);
        if (lunches == null) {
            return;
        }

        Timber.d("hello, iterating on %d lunches ", lunches.size());

        int i = 0;
        for (Lunch lunch : lunches) {
            Intent intent = getBroadcastIntent(lunch);

            //check if need to add 24h
            Calendar calendar = DateFormatters.getCalendarFromMinutesOfDay(lunch.getTimeOfDay());

            long time = calendar.getTimeInMillis();
            if (time < System.currentTimeMillis()) {

                Timber.d("Plus one day!");
                time += AlarmManager.INTERVAL_DAY;
            }

            Timber.d ("alarm scheduled for lunch %s at %d", lunch, time);

            PendingIntent  alarmIntent = PendingIntent.getBroadcast(context, i, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, time, AlarmManager.INTERVAL_DAY, alarmIntent);
            i++;
        }
    }

    @NonNull
    private Intent getBroadcastIntent(Lunch lunch) {
        Intent intent = getStandardIntent();
        intent.putExtra(AlertReceiver_.TIME_EXTRA, DateFormatters.formatMinutesOfDay(lunch.getTimeOfDay()));
        intent.putExtra(AlertReceiver_.CONTENT_EXTRA, lunch.getContent());
        return intent;
    }

    private Intent getStandardIntent() {
        Intent intent = new Intent(context, AlertReceiver_.class);
        intent.setAction(RECEIVER_ACTION);
        return intent;
    }

    public void cancelAllAlarms() {
        Intent intent = getStandardIntent();
        PendingIntent alarmIntent;
        for (int i = 0; i < 99; i++) {
            alarmIntent = PendingIntent.getBroadcast(context, i, intent, 0);
            alarmMgr.cancel(alarmIntent);
        }
    }

}
