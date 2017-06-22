package org.project.adam.glycaemia;

import android.annotation.SuppressLint;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.SeekBarProgressChange;
import org.androidannotations.annotations.ViewById;
import org.project.adam.R;

import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("Registered")
@EActivity(R.layout.input_glycaemia)
public class GlycaemiaActivity extends AppCompatActivity {

    private static final int DEFAULT_GLYCAEMIA = 70;
    private static final int MIN_GLYCAEMIA = 20;
    private static final int MAX_GLYCAEMIA = 150;

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

    private Date currentDate = new Date();

    @AfterViews
    void fillDateAndHour() {
        Date date = currentDate;

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
        glycaemiaHour.setText(hourOfDay + ":" + minute);
    }

    @Click(R.id.glycaemia_validate)
    void validate() {

    }
}
