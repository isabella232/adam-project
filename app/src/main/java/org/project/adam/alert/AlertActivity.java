package org.project.adam.alert;

import android.app.KeyguardManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.PowerManager;
import android.support.annotation.Nullable;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.SystemService;
import org.project.adam.BaseActivity;
import org.project.adam.MainActivity_;
import org.project.adam.R;

@EActivity(R.layout.activity_alert)
@Fullscreen
public class AlertActivity extends BaseActivity {

    @SystemService
    protected PowerManager powerManager;

    @SystemService
    protected KeyguardManager keyguardManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
      /*  PowerManager.WakeLock wakeLock = powerManager.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
        wakeLock.acquire();

        KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("TAG");
        keyguardLock.disableKeyguard();*/

        super.onCreate(savedInstanceState, persistentState);
    }

    @Click(R.id.alert_stop_button)
    public void goToMain(){
        MainActivity_.intent(this).start();
        finish();
    }
}
