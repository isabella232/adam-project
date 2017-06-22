package org.project.adam;

import org.androidannotations.annotations.sharedpreferences.DefaultInt;
import org.androidannotations.annotations.sharedpreferences.DefaultLong;
import org.androidannotations.annotations.sharedpreferences.SharedPref;


@SharedPref(value = SharedPref.Scope.UNIQUE)
public interface Preferences {
    public static int NO_MENU_SELECTED = -1;

    @DefaultInt(15)
    int reminderTimeInMinutes();

    @DefaultInt(NO_MENU_SELECTED)
    int currentMenuId();

    String recipientsEmails();

}
