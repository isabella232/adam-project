package org.project.adam;

import org.androidannotations.annotations.sharedpreferences.DefaultFloat;
import org.androidannotations.annotations.sharedpreferences.DefaultInt;
import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;
import org.project.adam.ui.dashboard.glycaemia.InputGlycaemiaActivity;
import org.project.adam.ui.diet.MealLoader;

import static org.project.adam.alert.AlertScheduler.DEFAULT_TIME_IN_MN;


@SharedPref(value = SharedPref.Scope.UNIQUE)
public interface Preferences {

    @DefaultInt(DEFAULT_TIME_IN_MN)
    int reminderTimeInMinutes();

    @DefaultInt(MainActivity.DEFAULT_DIET_ID)
    int currentDietId();

    @DefaultString(MealLoader.CSV_DEFAULT_SEPARATOR)
    String fieldSeparatorsForImport();

    String recipientsEmails();

    String alertRingtone();


    @DefaultInt(20)
    int minGly();

    @DefaultInt(60)
    int riskGly();

    @DefaultInt(120)
    int maxGly();

    @DefaultFloat(InputGlycaemiaActivity.DEFAULT_GLYCAEMIA)
    float lastGlycaemiaSet();

}
