package org.project.adam;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.KeyDown;
import org.project.adam.alert.AlertScheduler;

import timber.log.Timber;

/**
 * All the activities of the application should inherit this activity.
 */
@EActivity
public abstract class BaseActivity extends AppCompatActivity implements LifecycleRegistryOwner {

    private final LifecycleRegistry registry = new LifecycleRegistry(this);

    @Override
    public LifecycleRegistry getLifecycle() {
        return registry;
    }

    @Bean
    protected AlertScheduler alertScheduler;

    @KeyDown({ KeyEvent.KEYCODE_VOLUME_DOWN, KeyEvent.KEYCODE_VOLUME_UP })
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
                                Timber.d("Deleting data");;
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
}