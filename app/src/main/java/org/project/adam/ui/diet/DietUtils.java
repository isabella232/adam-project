package org.project.adam.ui.diet;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.project.adam.Preferences_;
import org.project.adam.persistence.Diet;

@EBean
public class DietUtils {

    @Pref
    Preferences_ preferences;

    boolean isCurrent(Diet diet) {
        return preferences.currentMenuId().getOr(-1) == diet.getId();
    }
}
