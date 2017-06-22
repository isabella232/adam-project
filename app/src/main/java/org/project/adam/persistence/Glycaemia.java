package org.project.adam.persistence;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(tableName = "glycaemias",
    indices = {@Index("context")})
public class Glycaemia {

    @PrimaryKey(autoGenerate = true)
    int id;

    Date date;

    String context;

    String comment;

    float value;

}
