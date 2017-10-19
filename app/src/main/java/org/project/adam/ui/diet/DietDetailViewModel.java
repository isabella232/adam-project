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

public class DietDetailViewModel extends AndroidViewModel {

    final AppDatabase appDatabase;

    @Getter
    private LiveData<Integer> itemCount;

    @Getter
    private LiveData<Diet> diet;

    @Getter
    private LiveData<List<Meal>> lunches;


    public DietDetailViewModel(Application application) {
        super(application);

        appDatabase = AppDatabase.getDatabase(application);
        itemCount = appDatabase.dietDao().count();
    }

    public void loadDiet(int dietId){
        if (diet == null){
            diet = appDatabase.dietDao().find(dietId);
            lunches = appDatabase.mealDao().findFromDiet(dietId);
        }
    }

    public void removeDiet() {

        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                appDatabase.dietDao().delete(diet.getValue());
                return null;
            }
        }.execute();
    }


}