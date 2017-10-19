package org.project.adam.ui.diet;
/**
 * Adam project
 * Copyright (C) 2017 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.project.adam.Preferences_;
import org.project.adam.alert.AlertScheduler;
import org.project.adam.persistence.Diet;

@EBean
public class DietUtils {

    @Pref
    Preferences_ preferences;

    @Bean
    AlertScheduler alertScheduler;

    void setCurrent(Diet diet) {
        preferences.edit()
            .currentDietId()
            .put(diet.getId())
            .apply();

        alertScheduler.schedule();
    }

    boolean isCurrent(Diet diet) {
        return isCurrent(diet.getId());
    }

    boolean isCurrent(int dietId) {
        return preferences.currentDietId().getOr(-1) == dietId;
    }
}
