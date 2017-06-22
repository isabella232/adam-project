package org.project.adam.persistence;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface DietDao {

    @Query("SELECT * from diets")
    LiveData<List<Diet>> findAll();

    @Query("SELECT * from diets where id=:id")
    LiveData<Diet> find(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insert(Diet... diets);

    @Query("SELECT COUNT (*) from diets")
    LiveData<Integer> count();

    @Delete
    void delete(Diet... diets);
}
