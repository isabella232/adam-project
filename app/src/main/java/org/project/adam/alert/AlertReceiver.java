package org.project.adam.alert;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EReceiver;
import org.androidannotations.annotations.ReceiverAction;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.api.support.content.AbstractBroadcastReceiver;
import org.project.adam.MainActivity_;
import org.project.adam.Preferences_;
import org.project.adam.R;

import java.util.Random;

import timber.log.Timber;

@EReceiver
public class AlertReceiver extends AbstractBroadcastReceiver {

    @SystemService
    protected NotificationManager notificationManager;

    @Bean
    protected AlertScheduler alertScheduler;

    @Pref
    protected Preferences_ prefs;


    private static final String GROUP = "Reminder";

    public static final String RECEIVER_ACTION = "org.project.adam.ALARM";

    @ReceiverAction(actions = Intent.ACTION_BOOT_COMPLETED)
    public void resetAlarms() {
        Timber.d("Boot detected, setting up alerts");
        alertScheduler.schedule();
    }

    @ReceiverAction(actions = RECEIVER_ACTION)
    void myAction(Intent intent, @ReceiverAction.Extra String time, @ReceiverAction.Extra String content, Context context) {
        Timber.i("ALARME RECEIVED for meal %s", time);

        PendingIntent pi = PendingIntent.getActivity(context, 0, MainActivity_.intent(context).get(), PendingIntent.FLAG_UPDATE_CURRENT);

        String body = String.format(context.getResources().getString(R.string.notif_content), time);

        NotificationCompat.Builder mBuilder =
            new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_reminder_notification)
                .setContentTitle(context.getString(R.string.notif_title))
                .setAutoCancel(true)
                .setSound(notificationSoundUri())
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentText(body)
                .setGroup(GROUP)
                .setContentIntent(pi)
                .setGroup("group");

        if (!TextUtils.isEmpty(content)) {
            mBuilder = mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(String.format("%s\n%s", body, content)));
        }

        Random r = new Random();
        notificationManager.notify(r.nextInt(), mBuilder.build());
    }

    private Uri notificationSoundUri() {
        String ringtoneUri = prefs.alertRingtone().getOr(null);

        if (ringtoneUri != null) {
            return Uri.parse(ringtoneUri);
        }

        return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    }


}
