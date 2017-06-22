package org.project.adam;

import android.support.v4.app.Fragment;
import android.widget.SeekBar;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by bastien on 22/06/2017.
 */
@EFragment(R.layout.fragment_meals_of_the_day)
public class MealsOfTheDayFragment extends Fragment {

    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("EEEE dd MMMM");

    @ViewById(R.id.date)
    TextView date;

    @ViewById(R.id.hours_of_day)
    SeekBar hoursOfDay;

    @Override
    public void onResume() {
        super.onResume();
        displayCurrentTime();
    }

    private void displayCurrentTime() {
        date.setText(DATE_FORMATTER.format(new Date()));
    }
}
