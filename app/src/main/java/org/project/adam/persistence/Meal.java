package org.project.adam.persistence;
/**
 * Adam project
 * Copyright (C) 2017 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import org.joda.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(tableName = "meals",
    foreignKeys = @ForeignKey(entity = Diet.class, parentColumns = "id", childColumns = "diet_id", onDelete = ForeignKey.CASCADE),
    indices = {@Index("diet_id"), @Index({"diet_id", "time_of_day"})})
public class Meal {
    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo(name = "diet_id")
    int dietId;

    // Minutes in a day
    @ColumnInfo(name = "time_of_day")
    LocalTime timeOfDay;

    String content;
}
