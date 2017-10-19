package org.project.adam.ui.dashboard.glycaemia;
/**
 * Adam project
 * Copyright (C) 2017 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import org.joda.time.LocalDateTime;
import org.project.adam.AppDatabase;
import org.project.adam.persistence.Glycaemia;

import java.util.List;

public class GlycaemiaViewModel extends AndroidViewModel {

    final AppDatabase appDatabase;

    private LiveData<List<Glycaemia>> glycaemias = new MutableLiveData<>();

    public GlycaemiaViewModel(Application application) {
        super(application);
        appDatabase = AppDatabase.getDatabase(application);
    }

    public LiveData<List<Glycaemia>> findGlycaemiaBetween(LocalDateTime min, LocalDateTime max) {
        glycaemias = appDatabase.glycemiaDao().findGlycaemiaBetween(min, max);
        return glycaemias;
    }


}
