package org.project.adam.glycaemia;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

import lombok.RequiredArgsConstructor;

//FIXME: use android annotation please
@RequiredArgsConstructor
public class TimePickerFragment extends DialogFragment
    implements TimePickerDialog.OnTimeSetListener {
    private final GlycaemiaActivity.Hour hour;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int h = hour == null ? c.get(Calendar.HOUR_OF_DAY) : hour.hourOfDay;
        int m = hour == null ? c.get(Calendar.MINUTE) : hour.minute;

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, h, m,
            DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        ((GlycaemiaActivity_)getActivity()).updateTime(hourOfDay, minute);
    }
}
