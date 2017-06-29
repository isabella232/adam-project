package org.project.adam;

import android.annotation.SuppressLint;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.os.AsyncTask;

import org.project.adam.persistence.Diet;
import org.project.adam.persistence.DietDao;
import org.project.adam.persistence.Glycaemia;
import org.project.adam.persistence.GlycaemiaDao;
import org.project.adam.persistence.Meal;
import org.project.adam.persistence.MealDao;
import org.project.adam.util.DatabasePopulator;
import org.project.adam.util.DateConverters;

@Database(entities = {Meal.class, Glycaemia.class, Diet.class},
    version = AppDatabase.DATABASE_VERSION,
    exportSchema = false)
@TypeConverters(DateConverters.class)
public abstract class AppDatabase extends RoomDatabase {

    static final int DATABASE_VERSION = 3;

    static final Migration RENAME_LUNCH_TABLE_TO_MEAL = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE lunches RENAME TO meals");
        }
    };

    private static final String DATABASE_NAME = "database";

    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                .addMigrations(RENAME_LUNCH_TABLE_TO_MEAL)
                .build();
            if (BuildConfig.DEBUG) {
                mockData(context, INSTANCE);
            }
        }
        return INSTANCE;
    }

    @SuppressLint("StaticFieldLeak")
    private static void mockData(Context context, final AppDatabase db) {
        new AsyncTask<Context, Void, Void>() {

            @Override
            protected Void doInBackground(Context... params) {

                // Add some data to the database
                DatabasePopulator.initializeDb(db);
                return null;
            }
        }.execute(context.getApplicationContext());
    }

    public abstract DietDao dietDao();

    public abstract GlycaemiaDao glycemiaDao();

    public abstract MealDao mealDao();

}
