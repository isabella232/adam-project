package org.project.adam;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.support.v4.app.Fragment;

import org.androidannotations.annotations.EFragment;

/**
 * All the fragment of the application should inherit this.
 */
@EFragment
public abstract class BaseFragment extends Fragment implements LifecycleRegistryOwner {

    LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);

    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }

}
