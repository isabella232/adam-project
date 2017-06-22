package org.project.adam.glycaemia;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.SeekBarProgressChange;
import org.androidannotations.annotations.ViewById;
import org.project.adam.AppDatabase;
import org.project.adam.R;
import org.project.adam.persistence.Glycaemia;
import org.project.adam.persistence.GlycaemiaDao;
import org.project.adam.util.DatabasePopulator;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lombok.RequiredArgsConstructor;

@SuppressLint("Registered")
@EActivity(R.layout.input_glycaemia)
public class GlycaemiaActivity extends AppCompatActivity {

    private static final int DEFAULT_GLYCAEMIA = 70;
    private static final int MIN_GLYCAEMIA = 20;
    private static final int MAX_GLYCAEMIA = 150;

    @RequiredArgsConstructor
    private class Hour {
        final int hourOfDay;
        final int minute;
    }


    @ViewById(R.id.glycaemia_date)
    TextView glycaemiaDate;

    @ViewById(R.id.glycaemia_hour)
    TextView glycaemiaHour;

    @ViewById(R.id.glycaemia_value_mg_Dl)
    TextView glycaemiaValueMgDl;

    @ViewById(R.id.glycaemia_validate)
    Button validateGlycaemia;

    // FIXME: should work with android annotations
    SeekBar seekBarGlycaemia;

    Hour hour;

    int lunchId = -1;

    @AfterViews
    void fillDateAndHour() {
        Date date = new Date();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM");
        glycaemiaDate.setText(simpleDateFormat.format(date));

        simpleDateFormat = new SimpleDateFormat("H: mm");
        glycaemiaHour.setText(simpleDateFormat.format(date));
    }

    @AfterViews
    void initSeekBar() {
        seekBarGlycaemia = (SeekBar) findViewById(R.id.glycaemia_seekBar);
        seekBarGlycaemia.setProgress(DEFAULT_GLYCAEMIA);
        seekBarGlycaemia.setMax(MAX_GLYCAEMIA - MIN_GLYCAEMIA);
        glycaemiaValueMgDl.setText(String.valueOf(DEFAULT_GLYCAEMIA));
    }

    @SeekBarProgressChange(R.id.glycaemia_seekBar)
    void onSeekBarProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        glycaemiaValueMgDl.setText(String.valueOf(progress + MIN_GLYCAEMIA));
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    void updateTime(int hourOfDay, int minute) {
        hour = new Hour(hourOfDay, minute);
        glycaemiaHour.setText(hourOfDay + ":" + minute);
    }

    @Click(R.id.glycaemia_validate)
    void validate() {
        if (lunchId < 0) {
            Toast.makeText(this, "Invalid lunch id", Toast.LENGTH_LONG).show();
            return;
        }

        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                GlycaemiaDao glycaemiaDao = AppDatabase.getDatabase(getApplicationContext()).glycemiaDao();
                glycaemiaDao.insert(buildGlycemia());
                return null;
            }
        }.execute();
    }

    private Glycaemia buildGlycemia() {
        float value = Float.parseFloat(glycaemiaValueMgDl.getText().toString());
        Date date = new Date();
        if (hour != null) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, hour.hourOfDay);
            cal.set(Calendar.MINUTE, hour.minute);
            cal.set(Calendar.SECOND, 0);
            date = cal.getTime();
        }

        Glycaemia glycaemia = Glycaemia.builder()
            .lunchId(lunchId)
            .date(date)
            .value(value)
            .comment("Not implemented yet")
            .context("Not implemented yet")
            .build();

        Log.d(getClass().getSimpleName(), glycaemia.toString());

        return glycaemia;
    }
}
