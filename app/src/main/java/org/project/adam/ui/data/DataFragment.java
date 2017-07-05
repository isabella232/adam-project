package org.project.adam.ui.data;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.widget.DatePicker;
import android.widget.TextView;

import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
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
import org.project.adam.ui.util.DatePickerFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import timber.log.Timber;

@SuppressLint("Registered")
@EFragment(R.layout.fragment_data)
public class DataFragment extends BaseFragment {
    public static final SimpleDateFormat DISPLAY_DATE_FORMAT = new SimpleDateFormat("EEE d MMM");

    public static final SimpleDateFormat MAIL_DATE_FORMAT = new SimpleDateFormat("HH:mm");

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

    private Date beginDate;

    private Date endDate;


    @AfterViews
    public void init() {
        glycaemiaViewModel = ViewModelProviders.of(this).get(GlycaemiaViewModel.class);
        beginDate = beginningOfDay(Calendar.getInstance());
        endDate = endOfDay(Calendar.getInstance());
        refreshDatesDisplayAndData();
    }

    public void refreshDatesDisplayAndData() {
        Timber.d("refreshDatesDisplayAndData - %s - %s", this.beginDate, this.endDate);
        fromDateLabel.setText(DISPLAY_DATE_FORMAT.format(this.beginDate));
        toDateLabel.setText(DISPLAY_DATE_FORMAT.format(this.endDate));
        refreshData();
    }

    private Date beginningOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    private Date endOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    protected void refreshData() {
        final Date min = this.beginDate;
        final Date max = this.endDate;
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
        String previousDate = "";
        for (Glycaemia glycaemia : glycaemias) {
            String date = DISPLAY_DATE_FORMAT.format(glycaemia.getDate());
            if (!date.equals(previousDate)) {
                mailContent += "\n" + DISPLAY_DATE_FORMAT.format(glycaemia.getDate()) + ":\n";
                previousDate = date;
            }
            mailContent += "- " + MAIL_DATE_FORMAT.format(glycaemia.getDate()) + "\t   " + glycaemia.getValue() + " " + unit + " \n";
        }
        Timber.d("Mail content %s", mailContent);

    }


    @Click(R.id.button_mail)
    public void sendMail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + prefs.recipientsEmails().get()));
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mail_subject));
        intent.putExtra(Intent.EXTRA_TEXT, mailContent);
        //emailIntent.putExtra(Intent.EXTRA_HTML_TEXT, body); //If you are using HTML in your body text

        startActivity(Intent.createChooser(intent, "Send Email"));
    }



    @Click(R.id.data_from_date_container)
    protected void openFromDatePicker() {
        openDatePicker(this.beginDate, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                DataFragment dataFragment = DataFragment.this;
                dataFragment.beginDate = beginningOfDay(calendar);
                dataFragment.refreshDatesDisplayAndData();
            }
        });
    }

    @Click(R.id.data_to_date_container)
    protected void openToDatePicker() {
        openDatePicker(this.endDate, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                DataFragment dataFragment = DataFragment.this;
                dataFragment.endDate = endOfDay(calendar);
                dataFragment.refreshDatesDisplayAndData();
            }
        });
    }

    private void openDatePicker(Date initDate, DatePickerDialog.OnDateSetListener listener) {
        DatePickerFragment fragment = new DatePickerFragment()
            .setInitDate(initDate)
            .setListener(listener);
        fragment.show(getActivity().getSupportFragmentManager(), "timePicker");
    }

    public void refreshGraph(List<Glycaemia> glycaemias, Date min, Date max) {
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

        if (normalEntries.size() != 0) {
            ScatterDataSet normalValuesSet = new ScatterDataSet(normalEntries, "");
            normalValuesSet.setColors(colorOK);
            setScatteredDataStyle(normalValuesSet);
            lineData.addDataSet(normalValuesSet);
        }

        if (dangerEntries.size() != 0) {
            ScatterDataSet dangerValuesSet = new ScatterDataSet(dangerEntries, "");
            dangerValuesSet.setColors(colorRisk);
            setScatteredDataStyle(dangerValuesSet);
            lineData.addDataSet(dangerValuesSet);
        }

        lineData.setDrawValues(false);

        chart.getAxisLeft().setAxisMinimum(0);
        chart.getAxisRight().setAxisMinimum(0);
        chart.getAxisLeft().setEnabled(true);
        chart.getAxisRight().setEnabled(true);
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
