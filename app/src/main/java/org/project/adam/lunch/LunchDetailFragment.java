package org.project.adam.lunch;

import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.project.adam.BaseFragment;
import org.project.adam.R;
import org.project.adam.persistence.Lunch;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@EFragment(R.layout.fragment_lunch)
public class LunchDetailFragment extends BaseFragment {

    private static final SimpleDateFormat HOUR_FORMATTER = new SimpleDateFormat("kk:mm");

    @ViewById(R.id.hour_of_day)
    TextView hourOfDay;

    @ViewById(R.id.content)
    TextView content;

    private Lunch lunch;

    public void bind(Lunch lunch) {
        this.lunch = lunch;
    }

    @AfterViews
    void init() {
        hourOfDay.setText(hourOfDay());
        content.setText(lunch.getContent());
    }

    private String hourOfDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.add(Calendar.MINUTE, lunch.getTimeOfDay());
        return HOUR_FORMATTER.format(calendar.getTime());
    }
}
