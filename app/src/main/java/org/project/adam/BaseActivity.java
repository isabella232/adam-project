package org.project.adam;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.support.v7.app.AppCompatActivity;

import org.androidannotations.annotations.EActivity;

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

}
