package org.project.adam.ui.dashboard;

import android.annotation.SuppressLint;

import org.androidannotations.annotations.EFragment;
import org.project.adam.BaseFragment;
import org.project.adam.R;

@SuppressLint("Registered")
@EFragment(R.layout.fragment_dashboard)
public class DashboardFragment extends BaseFragment {

    @Override
    protected boolean isTitleDisplayed() {
        return false;
    }
}
