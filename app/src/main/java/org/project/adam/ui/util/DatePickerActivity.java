package org.project.adam.ui.util;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.DatePicker;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.project.adam.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@SuppressLint("Registered")
@EActivity(R.layout.activity_date_picker)
public class DatePickerActivity extends AppCompatActivity {

    public static final String FROM_TO_LABEL_PARAMETER =  "DatePickerActivity.fromToLabel";

    public static final String INIT_DATE_PARAMETER =  "DatePickerActivity.initDate";

    public static final String RESULT_PARAMETER =  "DatePickerActivity.result";

    @Extra(FROM_TO_LABEL_PARAMETER)
    protected String fromToTextValue;


    @ViewById(R.id.date_pick_from_to_text)
    protected TextView fromToText;

    @ViewById
    protected DatePicker datePicker;

    @AfterViews
    protected void setTextLabel(){
        fromToText.setText(fromToTextValue == null ? "": fromToTextValue);
        Date initDate = (Date) getIntent().getExtras().get(INIT_DATE_PARAMETER);
        initCalendar(initDate == null ? new Date() : initDate);
    }

    private void initCalendar(Date initDate) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(initDate);
        datePicker.updateDate(calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Click(R.id.date_pick_cancel_btn)
    protected void onCancelClick(){
        setResult(RESULT_CANCELED);
        finish();
    }

    @Click(R.id.date_pick_ok_btn)
    protected void onOkClick(){
        Intent intent = new Intent();
        intent.putExtra(RESULT_PARAMETER, getSelectedDate());
        setResult(RESULT_OK, intent);
        finish();
    }

    @NonNull
    private Date getSelectedDate() {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.YEAR, datePicker.getYear());
        calendar.set(Calendar.MONTH, datePicker.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
        return calendar.getTime();
    }


}
