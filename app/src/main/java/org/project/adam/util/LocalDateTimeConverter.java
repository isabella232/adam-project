package org.project.adam.util;
/**
 * Adam project
 * Copyright (C) 2017 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import android.arch.persistence.room.TypeConverter;

import org.joda.time.LocalDateTime;

public class LocalDateTimeConverter {
    @TypeConverter
    public LocalDateTime fromTimestamp(Long value) {
        return value == null ? null : new LocalDateTime(value);
    }

    @TypeConverter
    public Long toTimestamp(LocalDateTime date) {
        return date == null ? null : date.toDate().getTime();
    }
}