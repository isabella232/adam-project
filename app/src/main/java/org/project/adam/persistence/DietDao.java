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
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface DietDao {

    @Query("SELECT * from diets")
    LiveData<List<Diet>> findAll();

    @Query("SELECT * from diets where id=:id")
    LiveData<Diet> find(int id);

    @Query("SELECT * from diets where id=:id")
    Diet findSync(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insert(Diet... diets);

    @Query("SELECT COUNT (*) from diets")
    LiveData<Integer> count();

    @Delete
    void delete(Diet... diets);
}
