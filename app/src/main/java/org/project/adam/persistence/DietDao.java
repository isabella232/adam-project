package org.project.adam.persistence;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface DietDao {

    @Query("SELECT * from diets")
    LiveData<List<Diet>> findAll();

    @Query("SELECT * from diets where current=1")
    LiveData<Diet> findCurent();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Diet... diets);
}
