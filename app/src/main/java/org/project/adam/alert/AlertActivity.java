package org.project.adam.alert;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.widget.TextView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;
import org.project.adam.BaseActivity;
import org.project.adam.MainActivity_;
import org.project.adam.R;

import timber.log.Timber;

import static android.view.WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD;
import static android.view.WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
import static android.view.WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;

//TODO audio stream : use alarm?
//TODO test not full on not pin
//TODO Handle Sound selection
@EActivity(R.layout.activity_alert)
@Fullscreen
@WindowFeature({TYPE_SYSTEM_OVERLAY,
    FLAG_DISMISS_KEYGUARD,
    FLAG_SHOW_WHEN_LOCKED})
public class AlertActivity extends BaseActivity {

    private static final int ALARM_DURATION_IN_S = 60;
    public static final String ACTION = "org.project.adam.STOP_RINGING_ALARM";
    public static final int ALARM_RINGING_NOTIFICATION = 777;

    @SystemService
    protected Vibrator vibrator;

    @SystemService
    protected NotificationManager notificationManager;

    @ViewById(R.id.next_meal_detail)
    protected TextView nextMealDetail;

    @ViewById(R.id.next_meal_time)
    protected TextView nextMealTime;

    @Extra
    protected String mealContent;

    @Extra
    protected String mealTime;

    protected MediaPlayer alarmPlayer;

    /*we need to cancel the notification whenever the user has the activity
    opened, but there is no way to detect this without a timer making sure
    that we passed on resume but didn't had the onPause right after
    (this will occur with a screen locked with a pin code: activity is started in the
    background, going init->start->resume->pause at once
     */
    private CountDownTimer cancelNotificationTimer = new CountDownTimer(500, 500) {
        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            dismissNotification();
        }
    };


    @AfterInject
    public void init() {
        Timber.d("OnInit");
        alarmPlayer = MediaPlayer.create(this, Settings.System.DEFAULT_ALARM_ALERT_URI);
        showNotif();

    }

    @AfterViews
    public void updateViews(){
        nextMealDetail.setText(mealContent);
        nextMealTime.setText(mealTime);
    }


    @Override
    protected void onStart() {
        Timber.d("OnStart");
        super.onStart();
        scream();
        scheduleAlarmStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Timber.d("OnResume");
        cancelNotificationTimer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Timber.d("OnPause");
        cancelNotificationTimer.cancel();
    }


    @Click({R.id.bell_icon,R.id.imageView})
    @Receiver(actions = "org.project.adam.STOP_RINGING_ALARM")
    protected void stopAndExit() {
        shutUp();
        dismissNotification();
        MainActivity_.intent(this).start();
        finish();
    }

    @Background
    protected void scream() {
        alarmPlayer.start();
        startVibrating();
    }

    protected void shutUp() {
        Timber.d("Shutting down the alarm");
        if (alarmPlayer != null && alarmPlayer.isPlaying()) {
            alarmPlayer.stop();
        }
        vibrator.cancel();
    }

    @Background(delay = ALARM_DURATION_IN_S * 1000)
    protected void scheduleAlarmStop() {
        shutUp();
    }


    private void showNotif() {
        PendingIntent stopAlarmPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION), 0);
        String body = String.format(getResources().getString(R.string.notif_content), mealTime);
        NotificationCompat.Builder mBuilder =
            new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_reminder_notification)
                .setContentTitle(getString(R.string.notif_title))
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(stopAlarmPendingIntent)
                .addAction(R.drawable.ic_notifications_off_white,"Stop alarm",stopAlarmPendingIntent);
        notificationManager.notify(ALARM_RINGING_NOTIFICATION, mBuilder.build());
    }

    private void dismissNotification(){
        Timber.d("Cancelling the notification");
        notificationManager.cancel(ALARM_RINGING_NOTIFICATION);
    }

    private void startVibrating(){
        //vibrator.vibrate(1000,new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build());
        vibrator.vibrate(new long[]{700,700},0);
    }


}
