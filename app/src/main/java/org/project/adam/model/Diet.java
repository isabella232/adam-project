package org.project.adam.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(tableName = "diets")
public class Diet {
    @PrimaryKey(autoGenerate = true)
    int id;

    String name;

    boolean current;
}
