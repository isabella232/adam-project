package org.project.adam.ui.dashboard.glycaemia;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.tistory.dwfox.dwrulerviewlibrary.utils.DWUtils;
import com.tistory.dwfox.dwrulerviewlibrary.view.ObservableHorizontalScrollView;
import com.tistory.dwfox.dwrulerviewlibrary.view.ScrollingValuePicker;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.ColorRes;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.joda.time.LocalDateTime;
import org.project.adam.AppDatabase;
import org.project.adam.BaseActivity;
import org.project.adam.Preferences_;
import org.project.adam.R;
import org.project.adam.persistence.Glycaemia;
import org.project.adam.ui.util.TimePickerFragment;
import org.project.adam.util.DateFormatter;

import timber.log.Timber;

@SuppressLint("Registered")
@EActivity(R.layout.input_glycaemia)
public class InputGlycaemiaActivity extends BaseActivity {

    @Pref
    protected Preferences_ prefs;

    public static final int DEFAULT_GLYCAEMIA = 70;

    private static final float LINE_RULER_MULTIPLE_SIZE = 2.5f;

    private static final int MULTIPLE_TYPE = 5;


    @ViewById(R.id.glycaemia_root_view)
    protected View glycaemiaRootView;

    @ViewById(R.id.glycaemia_date)
    protected TextView glycaemiaDate;

    @ViewById(R.id.glycaemia_hour)
    protected TextView glycaemiaHour;

    @ViewById(R.id.glycaemia_value_mg_Dl)
    protected TextView glycaemiaValueMgDl;

    @ViewById(R.id.glycaemia_seekBar)
    protected ScrollingValuePicker seekBarGlycaemia;

    @ViewById(R.id.glycaemia_validate)
    protected Button validateGlycaemia;

    @Pref
    protected Preferences_ preferences;

    private LocalDateTime time;

    @ColorRes(R.color.sunflower_yellow)
    protected int colorRisk;

    @ColorRes(R.color.glycaemia_green)
    protected int colorOK;

    @Bean
    protected DateFormatter dateFormatter;

    @AfterViews
    void fillDateAndHour() {
        time = LocalDateTime.now();
        glycaemiaDate.setText(dateFormatter.longFormatDay(time));
        glycaemiaHour.setText(dateFormatter.formatMinutesOfDay(time));
    }

    @AfterViews
    void initSeekBar() {
        float defaultGlycaemia = preferences.lastGlycaemiaSet().get();
        seekBarGlycaemia.setInitValue(defaultGlycaemia);
        glycaemiaValueMgDl.setText(String.valueOf(defaultGlycaemia));
        seekBarGlycaemia.setMaxValue(prefs.minGly().get(), prefs.maxGly().get());
        seekBarGlycaemia.setViewMultipleSize(LINE_RULER_MULTIPLE_SIZE);
        seekBarGlycaemia.setValueTypeMultiple(MULTIPLE_TYPE);
        seekBarGlycaemia.getScrollView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    seekBarGlycaemia.getScrollView().startScrollerTask();
                }
                return false;
            }
        });

        seekBarGlycaemia
            .setOnScrollChangedListener(new ObservableHorizontalScrollView.OnScrollChangedListener() {

                                            @Override
                                            public void onScrollChanged(ObservableHorizontalScrollView view, int l, int t) {
                                                glycaemiaValueMgDl.setText(String.valueOf(DWUtils.getValueAndScrollItemToCenter(seekBarGlycaemia.getScrollView()
                                                    , l
                                                    , t
                                                    , prefs.maxGly().get()
                                                    , prefs.minGly().get()
                                                    , seekBarGlycaemia.getViewMultipleSize())));

                                                int value = Integer.parseInt(glycaemiaValueMgDl.getText().toString());
                                                int color = value < prefs.riskGly().get() ? colorRisk : colorOK;
                                                glycaemiaRootView.setBackgroundColor(color);
                                            }


                                            @Override
                                            public void onScrollStopped(int l, int t) {
                                            }
                                        }
            );
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment()
            .setInitTime(time.toLocalTime())
            .setListener(new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    InputGlycaemiaActivity.this.updateTime(hourOfDay, minute);
                }
            });
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    void updateTime(int hourOfDay, int minute) {
        time = time.withHourOfDay(hourOfDay).withMinuteOfHour(minute);
        glycaemiaHour.setText(dateFormatter.formatMinutesOfDay(time));
    }

    @Click(R.id.glycaemia_validate)
    void validate() {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                InputGlycaemiaActivity currentActivity = InputGlycaemiaActivity.this;
                Glycaemia glycaemia = buildGlycaemia();
                AppDatabase.getDatabase(currentActivity).glycemiaDao().insert(glycaemia);
                currentActivity.preferences.lastGlycaemiaSet().put(glycaemia.getValue());
                currentActivity.finish();
                return null;
            }
        }.execute();
    }

    private Glycaemia buildGlycaemia() {
        float value = Float.parseFloat(glycaemiaValueMgDl.getText().toString());
        Glycaemia glycaemia = Glycaemia.builder()
            .date(time)
            .value(value)
            .build();

        Timber.d("Glycaemia built - %s", glycaemia.toString());
        return glycaemia;
    }
}
