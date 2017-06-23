package org.project.adam.ui.data;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.widget.TextView;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import timber.log.Timber;

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

    @ViewById(R.id.data_from_date_label)
    TextView fromDateLabel;

    @ViewById(R.id.data_to_date_label)
    TextView toDateLabel;

    String mailContent;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE d MMM");


    @AfterViews
    public void init() {
        glycaemiaViewModel = ViewModelProviders.of(this).get(GlycaemiaViewModel.class);
        Date now = new Date();
        refreshDates(roundDateToBeginningOfDay(now), roundDateToEndOfDay(now));

    }

    public void refreshDates(Date min, Date max) {
        fromDateLabel.setText(simpleDateFormat.format(min));
        toDateLabel.setText(simpleDateFormat.format(max));
        refreshData(min, max);
    }

    private Date roundDateToBeginningOfDay(Date date) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    private Date roundDateToEndOfDay(Date date) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    protected void refreshData(Date min, Date max) {
        glycaemiaViewModel.findGlycaemiaBetween(min, max)
            .observe(this, new Observer<List<Glycaemia>>() {
                @Override
                public void onChanged(@Nullable List<Glycaemia> glycaemias) {
                    prepareMail(glycaemias);
                    refreshGraph(glycaemias);
                }
            });
    }

    public void prepareMail(List<Glycaemia> glycaemias) {
        mailContent = "Liste des relevés: \n";
        for (Glycaemia glycaemia : glycaemias) {
            mailContent += " - " + glycaemia.getDate().toString() + "\t" + glycaemia.getValue() + " " + unit + " \n";
        }
        Timber.d("Mail content %s", mailContent);

    }


    @Click(R.id.button_mail)
    public void sendMail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + prefs.recipientsEmails().get()));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Relevés glycémie");
        intent.putExtra(Intent.EXTRA_TEXT, mailContent);
        //emailIntent.putExtra(Intent.EXTRA_HTML_TEXT, body); //If you are using HTML in your body text

        startActivity(Intent.createChooser(intent, "Send Email"));
    }

    public void refreshGraph(List<Glycaemia> glycaemias) {
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

        dataSet.setMode(LineDataSet.Mode.STEPPED);

        chart.getAxisLeft().setAxisMinimum(0);
        chart.getAxisRight().setAxisMinimum(0);
        chart.setData(lineData);
        chart.invalidate();
        
    }


}
