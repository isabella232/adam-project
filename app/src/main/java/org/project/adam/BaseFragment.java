package org.project.adam;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * All the fragment of the application should inherit this.
 */
@EFragment
public abstract class BaseFragment extends Fragment implements LifecycleRegistryOwner {

    @ViewById(R.id.toolbar)
    protected Toolbar toolbar;

    LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);

    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }

    @AfterViews
    protected void setUpTooolbar() {
        if (toolbar != null && getActivity() instanceof AppCompatActivity) {
            final AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
            appCompatActivity.setSupportActionBar(toolbar);
            appCompatActivity.getSupportActionBar().setDisplayShowTitleEnabled(isTitleDisplayed());
        }
    }

    protected boolean isTitleDisplayed() {
        return true;
    }
}
