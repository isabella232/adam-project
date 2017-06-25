package org.project.adam;

import org.androidannotations.annotations.sharedpreferences.DefaultFloat;
import org.androidannotations.annotations.sharedpreferences.DefaultInt;
import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;
import org.project.adam.ui.dashboard.glycaemia.InputGlycaemiaActivity;
import org.project.adam.ui.diet.DietLoader;


@SharedPref(value = SharedPref.Scope.UNIQUE)
public interface Preferences {

    @DefaultInt(15)
    int reminderTimeInMinutes();

    @DefaultInt(MainActivity.DEFAULT_DIET_ID)
    int currentDietId();

    @DefaultString(DietLoader.CSV_DEFAULT_SEPARATOR)
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
