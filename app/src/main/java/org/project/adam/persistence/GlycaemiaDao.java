package org.project.adam.persistence;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.Date;
import java.util.List;

@Dao
public interface GlycaemiaDao {

    @Query("SELECT * from glycaemias")
    LiveData<List<Glycaemia>> findAll();

    @Query("SELECT * from glycaemias where date > :min and date < :max")
    LiveData<List<Glycaemia>> findGlycaemiaBetween(Date min, Date max);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Glycaemia... glycaemias);
}
