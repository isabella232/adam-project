package org.project.adam.ui.util;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.DatePicker;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.joda.time.LocalDate;
import org.project.adam.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import lombok.Builder;
import lombok.Setter;
import lombok.experimental.Accessors;

@SuppressLint("Registered")
@Setter
@Accessors(chain = true)
public class DatePickerFragment extends DialogFragment {

    private DatePickerDialog.OnDateSetListener listener;

    private LocalDate initDate;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if(initDate == null) {
            initDate = LocalDate.now();
        }
        return new DatePickerDialog(getActivity(), listener,
            initDate.getYear(),
            initDate.getMonthOfYear(),
            initDate.getDayOfMonth());
    }
}
