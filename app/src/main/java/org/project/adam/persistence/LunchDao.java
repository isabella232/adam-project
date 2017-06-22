package org.project.adam.persistence;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface LunchDao {

    @Query("SELECT * from lunches")
    LiveData<List<Lunch>> findAll();

    @Query("SELECT * from lunches where diet_id = :dietId order by time_of_day")
    LiveData<List<Lunch>> findFromDiet(int dietId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Lunch... lunches);
}
