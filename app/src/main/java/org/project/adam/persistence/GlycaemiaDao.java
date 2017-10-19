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

import org.joda.time.LocalDateTime;

import java.util.List;

@Dao
public interface GlycaemiaDao {

    @Query("SELECT * from glycaemias")
    LiveData<List<Glycaemia>> findAll();

    @Query("SELECT * from glycaemias where date > :min and date < :max order by date")
    LiveData<List<Glycaemia>> findGlycaemiaBetween(LocalDateTime min, LocalDateTime max);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Glycaemia... glycaemias);
}
