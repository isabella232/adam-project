package org.project.adam.ui.diet;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.project.adam.Preferences_;
import org.project.adam.persistence.Diet;

@EBean
public class DietUtils {

    @Pref
    Preferences_ preferences;

    void clearCurrent() {
        preferences.edit()
            .currentDietId()
            .remove()
            .apply();
    }

    void setCurrent(Diet diet) {
        preferences.edit()
            .currentDietId()
            .put(diet.getId())
            .apply();
    }

    boolean isCurrent(Diet diet) {
        return isCurrent(diet.getId());
    }

    boolean isCurrent(int dietId) {
        return preferences.currentDietId().getOr(-1) == dietId;
    }
}
