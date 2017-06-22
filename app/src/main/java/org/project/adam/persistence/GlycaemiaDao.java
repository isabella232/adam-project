package org.project.adam.persistence;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface GlycaemiaDao {

    @Query("SELECT * from glycaemias")
    LiveData<List<Glycaemia>> findAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Glycaemia... glycaemias);
}
