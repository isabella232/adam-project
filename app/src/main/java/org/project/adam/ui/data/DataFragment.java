package org.project.adam.ui.data;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.project.adam.BaseFragment;
import org.project.adam.Preferences_;
import org.project.adam.R;
import org.project.adam.persistence.Glycaemia;
import org.project.adam.ui.dashboard.glycaemia.GlycaemiaViewModel;

import java.util.List;

@SuppressLint("Registered")
@EFragment(R.layout.fragment_data)
public class DataFragment extends BaseFragment {


    private GlycaemiaViewModel glycaemiaViewModel;

    @Pref
    protected Preferences_ prefs;

    @StringRes(R.string.glycaemia_unit)
    protected String unit;

    @AfterViews
    public void init(){

    }
    @Click(R.id.button_mail)
    public void sendMail(){
        glycaemiaViewModel = ViewModelProviders.of(this).get(GlycaemiaViewModel.class);
        glycaemiaViewModel.findAll()
            .observe(this, new Observer<List<Glycaemia>>() {
                @Override
                public void onChanged(@Nullable List<Glycaemia> glycaemias) {

                    String content = "Liste des relevés: \n";
                    for(Glycaemia glycaemia : glycaemias){
                        content+=" - "+glycaemia.getDate().toString()+"\t"+glycaemia.getValue()+" "+unit+" \n";
                    }

                    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + prefs.recipientsEmails().get()));
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Relevés glycémie");
                    intent.putExtra(Intent.EXTRA_TEXT, content);
//emailIntent.putExtra(Intent.EXTRA_HTML_TEXT, body); //If you are using HTML in your body text

                    startActivity(Intent.createChooser(intent, "Send Email"));
                }
            });


    }

}
