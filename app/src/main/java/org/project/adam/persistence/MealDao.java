package org.project.adam.persistence;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MealDao {

    @Query("SELECT * from lunches")
    LiveData<List<Meal>> findAll();

    @Query("SELECT * from lunches where diet_id = :dietId order by time_of_day")
    LiveData<List<Meal>> findFromDiet(int dietId);

    @Query("SELECT * from lunches where diet_id = :dietId order by time_of_day")
    List<Meal> findFromDietSync(int dietId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Meal... meals);
}
