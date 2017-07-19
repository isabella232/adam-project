package org.project.adam;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.KeyDown;
import org.androidannotations.annotations.SystemService;
import org.project.adam.alert.AlertScheduler;

import timber.log.Timber;

/**
 * All the activities of the application should inherit this activity.
 */
@EActivity
public abstract class BaseActivity extends AppCompatActivity implements LifecycleRegistryOwner {

    private final LifecycleRegistry registry = new LifecycleRegistry(this);

    @SystemService
    protected SensorManager sensorManager;

    protected Sensor accelerometer;

    @Override
    public LifecycleRegistry getLifecycle() {
        return registry;
    }

    @Bean
    protected AlertScheduler alertScheduler;

    @KeyDown({KeyEvent.KEYCODE_VOLUME_DOWN, KeyEvent.KEYCODE_VOLUME_UP})
    protected boolean volumeLongPress(KeyEvent keyEvent) {
        if (BuildConfig.DEBUG && keyEvent.isLongPress()) {
            Timber.d("Did someone called for the secret menu?");
            showDevMenu();
            return true;
        }
        return false;
    }

    public void showDevMenu() {
        if (BuildConfig.DEBUG) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Dev tools")
                .setItems(new CharSequence[]{"Delete data", "Add sample diets", "Add sample glycemia measures", "Schedule alert in 15s"}, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Timber.d("Deleting data");
                                AppDatabase.getDatabase(BaseActivity.this).delete(BaseActivity.this);
                                break;
                            case 1:
                                Timber.d("Adding sample diets");
                                AppDatabase.getDatabase(BaseActivity.this).mockDiet(BaseActivity.this);
                                break;
                            case 2:
                                Timber.d("Adding glycemia measures");
                                AppDatabase.getDatabase(BaseActivity.this).mockGlycaemia(BaseActivity.this);
                                break;
                            case 3:
                                Timber.d("Scheduling fake alert");
                                alertScheduler.setUpFakeAlarm();
                                break;
                            default:
                                break;
                        }
                    }
                });
            builder.show();
        }
    }

    private SensorEventListener accelerometerListener = new SensorEventListener() {

        boolean hasBeenDown = false;

        @Override
        public void onSensorChanged(SensorEvent event) {
            float z = event.values[2];
            if (z > 9 && z < 10){
                if(hasBeenDown){
                    showDevMenu();
                    hasBeenDown = false;
                }
            }else if (z > -10 && z < -9){
                hasBeenDown = true;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor arg0, int arg1) {

        }

    };


    @Override
    protected void onResume() {
        super.onResume();
        if (BuildConfig.DEBUG) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
            if (accelerometer != null) {
                sensorManager.registerListener(accelerometerListener, accelerometer, SensorManager.SENSOR_DELAY_UI);
            }

        }
    }

    @Override
    protected void onPause() {
        if (BuildConfig.DEBUG) {
            sensorManager.unregisterListener(accelerometerListener);
        }
        super.onPause();
    }
}