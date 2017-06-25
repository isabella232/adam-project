package org.project.adam.ui.util;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;


import org.project.adam.ui.dashboard.glycaemia.InputGlycaemiaActivity;

import java.util.Calendar;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.Value;
import lombok.experimental.Accessors;

@Setter
@Accessors(chain = true)
public class TimePickerFragment extends DialogFragment {

    @RequiredArgsConstructor
    @Value
    public static class Hour {
        private int hourOfDay;

        private int minute;
    }

    private Hour hour;

    private TimePickerDialog.OnTimeSetListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int h = hour == null ? c.get(Calendar.HOUR_OF_DAY) : hour.hourOfDay;
        int m = hour == null ? c.get(Calendar.MINUTE) : hour.minute;

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), listener, h, m,
            DateFormat.is24HourFormat(getActivity()));
    }
}
