package org.project.adam;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import org.project.adam.persistence.Diet;
import org.project.adam.persistence.DietDao;
import org.project.adam.persistence.Glycaemia;
import org.project.adam.persistence.GlycaemiaDao;
import org.project.adam.persistence.Lunch;
import org.project.adam.persistence.LunchDao;
import org.project.adam.util.DateConverters;

@Database(entities = {Lunch.class, Glycaemia.class, Diet.class}, version = 1)
@TypeConverters(DateConverters.class)
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
    public abstract GlycaemiaDao glycemiaDao();
    public abstract LunchDao lunchDao();

}
