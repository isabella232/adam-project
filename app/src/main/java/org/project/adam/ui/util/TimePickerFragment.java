package org.project.adam.ui.util;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;

import org.joda.time.LocalTime;

import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Accessors(chain = true)
public class TimePickerFragment extends DialogFragment {

    private LocalTime initTime;

    private TimePickerDialog.OnTimeSetListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if(initTime == null){
            initTime = LocalTime.now();
        }
        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(),
            listener,
            initTime.getHourOfDay(),
            initTime.getMinuteOfHour(),
            DateFormat.is24HourFormat(getActivity()));
    }
}
