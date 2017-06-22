package org.project.adam.ui.diet;

import android.annotation.SuppressLint;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.project.adam.BaseFragment;
import org.project.adam.R;

import timber.log.Timber;

@SuppressLint("Registered")
@EFragment(R.layout.fragment_diet)
public class DietFragment extends BaseFragment {


    @Click(R.id.add_diet)
    public void onAddDietClick(){
        Timber.d("add diet clicked");
    }

}
