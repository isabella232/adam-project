package org.project.adam.alert;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.joda.time.LocalDateTime;
import org.project.adam.AppDatabase;
import org.project.adam.Preferences_;
import org.project.adam.persistence.Meal;
import org.project.adam.util.DateFormatter;

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

    @Bean
    protected DateFormatter dateFormatter;

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
        setUpAlarms(dietId);
    }

    @Background
    protected void setUpAlarms(int dietId) {

        Timber.d("Setting up alarms");

        List<Meal> meals = appDatabase.mealDao().findFromDietSync(dietId);
        if (meals == null) {
            return;
        }

        Timber.d("hello, iterating on %d meals ", meals.size());

        int i = 0;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime initDayTime = now.withTime(0,0,0,0);
        Integer reminderDelay = preferences.reminderTimeInMinutes().getOr(DEFAULT_TIME_IN_MN);
        for (Meal meal : meals) {
            LocalDateTime mealTime = initDayTime.plusMinutes(meal.getTimeOfDay().getMillisOfDay()/1000 - reminderDelay);
            Intent intent = getBroadcastIntent(mealTime, meal.getContent());
            if(mealTime.isBefore(now)){
                mealTime = mealTime.plusDays(1);
            }
            Timber.d("alarm scheduled for meal %s at %s", meal, mealTime);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(context, i, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            setUpAlarm(mealTime, alarmIntent);
            i++;
        }
    }

    public void setUpFakeAlarm() {
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 99, getStandardIntent(), PendingIntent.FLAG_UPDATE_CURRENT);
        setUpAlarm(LocalDateTime.now().plusSeconds(15), alarmIntent);
    }

    private void setUpAlarm(LocalDateTime time, PendingIntent alarmIntent) {
        long timeInMilliseconds = time.toDate().getTime();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMilliseconds, alarmIntent);
        } else {
            alarmMgr.setExact(AlarmManager.RTC_WAKEUP, timeInMilliseconds, alarmIntent);
        }
    }


    @NonNull
    private Intent getBroadcastIntent(LocalDateTime mealTime, String content) {
        Intent intent = getStandardIntent();
        intent.putExtra(AlertReceiver_.TIME_EXTRA, dateFormatter.hourOfDayFormat(mealTime));
        intent.putExtra(AlertReceiver_.CONTENT_EXTRA, content);
        return intent;
    }

    private Intent getStandardIntent() {
        Intent intent = new Intent(context, AlertReceiver_.class);
        intent.setAction(RECEIVER_ACTION);
        return intent;
    }

    private void cancelAllAlarms() {
        Intent intent = getStandardIntent();
        PendingIntent alarmIntent;
        for (int i = 0; i < 99; i++) {
            alarmIntent = PendingIntent.getBroadcast(context, i, intent, 0);
            alarmMgr.cancel(alarmIntent);
        }
    }

}
