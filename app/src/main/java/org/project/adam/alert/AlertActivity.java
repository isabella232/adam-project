package org.project.adam.alert;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.widget.SeekBar;
import android.widget.TextView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.project.adam.BaseActivity;
import org.project.adam.MainActivity_;
import org.project.adam.Preferences_;
import org.project.adam.R;

import java.io.IOException;

import timber.log.Timber;

import static android.view.WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD;
import static android.view.WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
import static android.view.WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;

@EActivity(R.layout.activity_alert)
@WindowFeature({TYPE_SYSTEM_OVERLAY,
    FLAG_DISMISS_KEYGUARD,
    FLAG_SHOW_WHEN_LOCKED})
public class AlertActivity extends BaseActivity {

    private static final int ALARM_DURATION_IN_S = 60;
    public static final String ACTION_STOP_ALARM = "org.project.adam.STOP_RINGING_ALARM";
    public static final int ALARM_RINGING_NOTIFICATION = 777;

    @SystemService
    protected Vibrator vibrator;

    @SystemService
    protected NotificationManager notificationManager;

    @ViewById(R.id.next_meal_detail)
    protected TextView nextMealDetail;

    @ViewById(R.id.next_meal_time)
    protected TextView nextMealTime;

    @ViewById(R.id.slide_to_unlock)
    protected SeekBar seekBar;

    @Extra
    protected String mealContent;

    @Extra
    protected String mealTime;

    @Pref
    protected Preferences_ prefs;

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
        //alarmPlayer = MediaPlayer.create(this, Settings.System.DEFAULT_ALARM_ALERT_URI); // this will not work as setting audio stream after datasource doesn't work...
        if (alarmPlayer == null) {
            alarmPlayer = new MediaPlayer();
            alarmPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
            try {
                alarmPlayer.setDataSource(this, alarmSoundUri());
                alarmPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        showNotif();

    }

    @AfterViews
    public void updateViews() {
        nextMealDetail.setText(mealContent);
        nextMealTime.setText(mealTime);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                if (seekBar.getProgress() > 95) {
                    stopAndExit();
                } else {
                    //bring it back to the start
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        seekBar.setProgress(0, true);
                    } else {
                        seekBar.setProgress(0);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {

            }
        });
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
        PendingIntent gotoActivityIntent = PendingIntent.getBroadcast(this, 0, AlertActivity_.intent(this).get(), 0);
        PendingIntent stopAlarmPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_STOP_ALARM), 0);
        String body = String.format(getResources().getString(R.string.notif_content), mealTime);
        NotificationCompat.Builder mBuilder =
            new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_reminder_notification)
                .setContentTitle(getString(R.string.notif_title))
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(gotoActivityIntent)
                .addAction(R.drawable.ic_notifications_off_white, "Stop alarm", stopAlarmPendingIntent);
        notificationManager.notify(ALARM_RINGING_NOTIFICATION, mBuilder.build());
    }

    private void dismissNotification() {
        Timber.d("Cancelling the notification");
        notificationManager.cancel(ALARM_RINGING_NOTIFICATION);
    }

    private void startVibrating() {
        vibrator.vibrate(new long[]{700, 700}, 0);
    }


    private Uri alarmSoundUri() {
        String ringtoneUri = prefs.alarmRingtone().getOr(null);
        if (ringtoneUri != null) {
            return Uri.parse(ringtoneUri);
        }
        return Settings.System.DEFAULT_ALARM_ALERT_URI;
    }

}
