package org.project.adam.persistence;
/**
 * Adam project
 * Copyright (C) 2017 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MealDao {

    @Query("SELECT * from meals")
    LiveData<List<Meal>> findAll();

    @Query("SELECT * from meals where diet_id = :dietId order by time_of_day")
    LiveData<List<Meal>> findFromDiet(int dietId);

    @Query("SELECT * from meals where diet_id = :dietId order by time_of_day")
    List<Meal> findFromDietSync(int dietId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Meal... meals);
}
