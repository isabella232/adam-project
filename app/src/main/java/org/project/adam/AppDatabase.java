package org.project.adam;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import org.project.adam.model.Diet;
import org.project.adam.model.DietDao;
import org.project.adam.model.Glycemia;
import org.project.adam.model.GlycemiaDao;
import org.project.adam.model.Lunch;
import org.project.adam.model.LunchDao;

@Database(entities = {Lunch.class, Glycemia.class, Diet.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "database";
    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase (Context context){
        if (INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME).build();
        }
        return INSTANCE;
    }

    public abstract DietDao dietDao();
    public abstract GlycemiaDao glycemiaDao();
    public abstract LunchDao lunchDao();

}
