package org.project.adam.ui.data;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.project.adam.BaseFragment;
import org.project.adam.Preferences_;
import org.project.adam.R;
import org.project.adam.persistence.Glycaemia;
import org.project.adam.ui.dashboard.glycaemia.GlycaemiaViewModel;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("Registered")
@EFragment(R.layout.fragment_data)
public class DataFragment extends BaseFragment {

    private GlycaemiaViewModel glycaemiaViewModel;

    @Pref
    protected Preferences_ prefs;

   @StringRes(R.string.glycaemia_unit)
    protected String unit;

    @ViewById(R.id.chart)
    LineChart chart;

    String mailContent;

    @AfterViews
    public void init() {
        glycaemiaViewModel = ViewModelProviders.of(this).get(GlycaemiaViewModel.class);
        glycaemiaViewModel.findAll()
            .observe(this, new Observer<List<Glycaemia>>() {
                @Override
                public void onChanged(@Nullable List<Glycaemia> glycaemias) {
                    prepareMail(glycaemias);
                    graph(glycaemias);

                }
            });

    }

    public void prepareMail(List<Glycaemia> glycaemias) {
         mailContent = "Liste des relevés: \n";
        for (Glycaemia glycaemia : glycaemias) {
            mailContent += " - " + glycaemia.getDate().toString() + "\t" + glycaemia.getValue() + " " + unit + " \n";
        }


    }


    @Click(R.id.button_mail)
    public void sendMail(){
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + prefs.recipientsEmails().get()));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Relevés glycémie");
        intent.putExtra(Intent.EXTRA_TEXT, mailContent);
//emailIntent.putExtra(Intent.EXTRA_HTML_TEXT, body); //If you are using HTML in your body text

        startActivity(Intent.createChooser(intent, "Send Email"));
    }

    public void graph(List<Glycaemia> glycaemias) {
        ArrayList<BarEntry> yValues = new ArrayList<>();

        List<Entry> entries = new ArrayList<>();
        for (final Glycaemia glycaemia : glycaemias) {

            float value = glycaemia.getValue();

            entries.add(new Entry(glycaemia.getDate().getTime(), value));
        }
        LineDataSet dataSet = new LineDataSet(entries, ""); // add entries to dataset
        dataSet.setColors(Color.BLUE);
        dataSet.setValueTextSize(11f);
        LineData lineData = new LineData(dataSet);
        lineData.setDrawValues(true);
        chart.setData(lineData);
        chart.invalidate();


    }



}
