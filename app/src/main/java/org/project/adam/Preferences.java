package org.project.adam;

import org.androidannotations.annotations.sharedpreferences.DefaultInt;
import org.androidannotations.annotations.sharedpreferences.DefaultLong;
import org.androidannotations.annotations.sharedpreferences.SharedPref;


@SharedPref(value = SharedPref.Scope.UNIQUE)
public interface Preferences {

    @DefaultLong(15)
    long reminderTimeInMinutes();

    String recipientsEmails();

}
