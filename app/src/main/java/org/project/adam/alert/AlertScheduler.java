package org.project.adam.alert;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.project.adam.AppDatabase;
import org.project.adam.Preferences_;
import org.project.adam.persistence.Meal;
import org.project.adam.util.DateFormatters;

import java.util.Calendar;
import java.util.List;

import timber.log.Timber;

import static org.project.adam.alert.AlertReceiver.RECEIVER_ACTION;


@EBean
public class AlertScheduler {

    public static final int DEFAULT_TIME_IN_MN = 15;

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

        List<Meal> meals = appDatabase.mealDao().findFromDietSync(dietId);
        if (meals == null) {
            return;
        }

        Timber.d("hello, iterating on %d meals ", meals.size());

        int i = 0;
        for (Meal meal : meals) {
            Intent intent = getBroadcastIntent(meal);

            Calendar calendar = DateFormatters.getCalendarFromMinutesOfDay(meal.getTimeOfDay() - preferences.reminderTimeInMinutes().getOr(DEFAULT_TIME_IN_MN));
            long time = calendar.getTimeInMillis();

            //check if need to add 24h
            if (time < System.currentTimeMillis()) {

                Timber.d("Plus one day!");
                time += AlarmManager.INTERVAL_DAY;
            }

            Timber.d("alarm scheduled for meal %s at %d", meal, time);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(context, i, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            setupAlarm(time, alarmIntent);
            i++;
        }
    }

    public void setupFakeAlarm() {
        long inAFewSeconds = Calendar.getInstance().getTimeInMillis() + 15 * 1000;
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 99, getStandardIntent(), PendingIntent.FLAG_UPDATE_CURRENT);
        setupAlarm(inAFewSeconds, alarmIntent);
    }

    private void setupAlarm(long time, PendingIntent alarmIntent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, alarmIntent);
        } else {
            alarmMgr.setExact(AlarmManager.RTC_WAKEUP, time, alarmIntent);
        }
    }


    @NonNull
    private Intent getBroadcastIntent(Meal meal) {
        Intent intent = getStandardIntent();
        intent.putExtra(AlertReceiver_.TIME_EXTRA, DateFormatters.formatMinutesOfDay(meal.getTimeOfDay()));
        intent.putExtra(AlertReceiver_.CONTENT_EXTRA, meal.getContent());
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
