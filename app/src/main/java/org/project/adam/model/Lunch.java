package org.project.adam.model;

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
@Entity(tableName = "lunches",
    foreignKeys = @ForeignKey(entity = Diet.class, parentColumns = "id", childColumns = "diet_id"),
    indices = @Index("diet_id"))
public class Lunch {
    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo(name = "diet_id")
    int dietId;
}
