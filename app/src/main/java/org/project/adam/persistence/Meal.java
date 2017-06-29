package org.project.adam.persistence;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

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
    int timeOfDay;

    String content;
}
