package org.project.adam;

import org.androidannotations.annotations.sharedpreferences.DefaultInt;
import org.androidannotations.annotations.sharedpreferences.SharedPref;


@SharedPref(value = SharedPref.Scope.UNIQUE)
public interface Preferences {

    @DefaultInt(15)
    int reminderTimeInMinutes();

    @DefaultInt(-1)
    int currentMenuId();

    String recipientsEmails();


    @DefaultInt(20)
    int minGly();

    @DefaultInt(60)
    int riskGly();

    @DefaultInt(120)
    int maxGly();

}
