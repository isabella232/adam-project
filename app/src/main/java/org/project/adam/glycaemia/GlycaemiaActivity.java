package org.project.adam.glycaemia;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.project.adam.R;

import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("Registered")
@EActivity(R.layout.input_glycaemia)
public class GlycaemiaActivity extends AppCompatActivity {

    @ViewById(R.id.glycaemia_date)
    EditText glycaemiaDate;

    @ViewById(R.id.glycaemia_hour)
    EditText glycaemiaHour;

    @ViewById(R.id.glycaemia_value_mg_Dl)
    EditText glycaemiaValueMgDl;

    @ViewById(R.id.glycaemia_validate)
    Button validateGlycaemia;

    private Date currentDate = new Date();

    @AfterViews
    void fillDateAndHour() {
        Date date = currentDate;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM");
        glycaemiaDate.setText(simpleDateFormat.format(date));

        simpleDateFormat = new SimpleDateFormat("h: mm");
        glycaemiaHour.setText(simpleDateFormat.format(date));
    }

    @Click(R.id.glycaemia_validate)
    void validate() {

    }
}
