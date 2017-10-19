package org.project.adam.ui.diet;
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
import android.os.AsyncTask;

import org.project.adam.AppDatabase;
import org.project.adam.persistence.Diet;
import org.project.adam.persistence.Meal;

import java.util.List;

import lombok.Getter;
import timber.log.Timber;

public class DietListViewModel extends AndroidViewModel {

    final AppDatabase appDatabase;

    @Getter
    private LiveData<List<Diet>> diets;

    public DietListViewModel(Application application) {
        super(application);

        appDatabase = AppDatabase.getDatabase(application);
        diets = appDatabase.dietDao().findAll();
    }

    public void createDiet(final Diet diet, final Meal... meals){
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                List<Long> generatedIds = appDatabase.dietDao().insert(diet);
                Timber.w("Diet %d", generatedIds.size());
                if(generatedIds.size() == 1
                    && generatedIds.get(0) != null && generatedIds.get(0) != 0){
                    for(Meal meal : meals){
                        meal.setDietId(generatedIds.get(0).intValue());
                    }
                    appDatabase.mealDao().insert(meals);
                }
                return null;
            }
        }.execute();
    }

}