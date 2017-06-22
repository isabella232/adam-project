package org.project.adam.ui.dashboard;

import android.annotation.SuppressLint;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.project.adam.BaseFragment;
import org.project.adam.R;
import org.project.adam.glycaemia.GlycaemiaActivity_;

@SuppressLint("Registered")
@EFragment(R.layout.fragment_dashboard)
public class DashboardFragment extends BaseFragment {

    @Click(R.id.model_sample)
    void showDietModelSample (){
        DietSampleActivity_.intent(this).start();
    }
}
