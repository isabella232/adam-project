package org.project.adam;

import android.annotation.SuppressLint;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.project.adam.glycaemia.GlycaemiaActivity_;
import org.project.adam.sample.DietSampleActivity_;

@SuppressLint("Registered")
@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {


    @Click(R.id.model_sample)
    void showDietModelSample (){
        DietSampleActivity_.intent(this).start();
    }

    @Click(R.id.start_glycaemia_picker)
    void startGlycaemiaPicker (){
        GlycaemiaActivity_.intent(this).start();
    }
}
