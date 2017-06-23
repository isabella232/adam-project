package org.project.adam.ui.data;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.ColorRes;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.project.adam.BaseFragment;
import org.project.adam.Preferences_;
import org.project.adam.R;
import org.project.adam.persistence.Glycaemia;
import org.project.adam.ui.dashboard.glycaemia.GlycaemiaViewModel;

import java.text.DateFormat;
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

    @StringRes(R.string.mail_header)
    protected String mailHeader;

    @ColorRes(R.color.sunflower_yellow)
    int colorRisk;

    @ColorRes(R.color.glycaemia_green)
    int colorOK;

    @ViewById(R.id.chart)
    ScatterChart chart;

    @ViewById(R.id.data_from_date_label)
    TextView fromDateLabel;

    @ViewById(R.id.data_to_date_label)
    TextView toDateLabel;

    String mailContent;

    public static final SimpleDateFormat DISPLAY_DATE_FORMAT = new SimpleDateFormat("EEE d MMM");
    public static final SimpleDateFormat MAIL_DATE_FORMAT = new SimpleDateFormat("EEE d MMM HH:mm");


    @AfterViews
    public void init() {
        glycaemiaViewModel = ViewModelProviders.of(this).get(GlycaemiaViewModel.class);
        Date now = new Date();
        refreshDates(roundDateToBeginningOfDay(now), roundDateToEndOfDay(now));

    }

    public void refreshDates(Date min, Date max) {
        fromDateLabel.setText(DISPLAY_DATE_FORMAT.format(min));
        toDateLabel.setText(DISPLAY_DATE_FORMAT.format(max));
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

    protected void refreshData(final Date min, final Date max) {
        glycaemiaViewModel.findGlycaemiaBetween(min, max)
            .observe(this, new Observer<List<Glycaemia>>() {
                @Override
                public void onChanged(@Nullable List<Glycaemia> glycaemias) {
                    prepareMail(glycaemias);
                    refreshGraph(glycaemias, min, max);
                }
            });
    }

    public void prepareMail(List<Glycaemia> glycaemias) {
        mailContent = mailHeader + " \n";
        for (Glycaemia glycaemia : glycaemias) {
            mailContent += " - " + MAIL_DATE_FORMAT.format(glycaemia.getDate()) + "\t    " + glycaemia.getValue() + " " + unit + " \n";
        }
        Timber.d("Mail content %s", mailContent);

    }


    @Click(R.id.button_mail)
    public void sendMail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + prefs.recipientsEmails().get()));
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mail_subject));
        intent.putExtra(Intent.EXTRA_TEXT, mailContent);
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
        intent.putExtra(Intent.EXTRA_HTML_TEXT, "<b>This is </b> funny html"); //If you are using HTML in your body text

        startActivity(Intent.createChooser(intent, "Send Email"));
    }

    public void refreshGraph(List<Glycaemia> glycaemias, Date min, Date max) {
        ArrayList<BarEntry> yValues = new ArrayList<>();

        List<Entry> normalEntries = new ArrayList<>();
        List<Entry> dangerEntries = new ArrayList<>();
        for (final Glycaemia glycaemia : glycaemias) {

            float value = glycaemia.getValue();
            if (value <= prefs.riskGly().get()) {
                dangerEntries.add(new Entry(glycaemia.getDate().getTime(), value));
            } else {
                normalEntries.add(new Entry(glycaemia.getDate().getTime(), value));
            }

        }
        ScatterData lineData = new ScatterData();

        if (normalEntries.size() != 0){
            ScatterDataSet normalValuesSet = new ScatterDataSet(normalEntries, "");
            normalValuesSet.setColors(colorOK);
            setScatteredDataStyle(normalValuesSet);
            lineData.addDataSet(normalValuesSet);
        }

        if (dangerEntries.size() != 0){
            ScatterDataSet dangerValuesSet = new ScatterDataSet(dangerEntries, "");
            dangerValuesSet.setColors(colorRisk);
            setScatteredDataStyle(dangerValuesSet);
            lineData.addDataSet(dangerValuesSet);
        }

        lineData.setDrawValues(true);

        chart.getAxisLeft().setAxisMinimum(0);
        chart.getAxisRight().setAxisMinimum(0);
        chart.getAxisLeft().setEnabled(false);
        chart.getAxisRight().setEnabled(false);
        IAxisValueFormatter xAxisFormatter = new DateAxisValueFormatter();
        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(xAxisFormatter);
        xAxis.setLabelRotationAngle(-45);
        xAxis.setDrawGridLines(false);
        chart.setData(lineData);
        chart.invalidate();
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getLegend().setEnabled(false);

        chart.getXAxis().setAxisMinimum(min.getTime());
        chart.getXAxis().setAxisMaximum(max.getTime());


    }

    private void setScatteredDataStyle(ScatterDataSet set) {
        set.setValueTextSize(11f);
        set.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
        set.setScatterShapeSize(45);
    }


    class DateAxisValueFormatter implements IAxisValueFormatter {

        private DateFormat mDataFormat = new SimpleDateFormat("dd/MM HH:mm");
        private Date mDate = new Date();

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            mDate.setTime((long) value);
            return mDataFormat.format(mDate);
        }
    }


}
