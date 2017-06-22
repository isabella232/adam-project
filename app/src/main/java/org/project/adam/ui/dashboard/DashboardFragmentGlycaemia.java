package org.project.adam.ui.dashboard;

import android.annotation.SuppressLint;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.project.adam.BaseFragment;
import org.project.adam.R;
import org.project.adam.glycaemia.GlycaemiaActivity_;
import org.project.adam.sample.DietSampleActivity_;

@SuppressLint("Registered")
@EFragment(R.layout.fragment_dashboard_glycaemia)
public class DashboardFragmentGlycaemia extends BaseFragment {

    @Click(R.id.dashboard_input_glycaemia)
    void showGlycaemiaPicker (){
        GlycaemiaActivity_.intent(this).start();
    }
}
