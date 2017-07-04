package org.project.adam.util;


import org.project.adam.AppDatabase;
import org.project.adam.persistence.Diet;
import org.project.adam.persistence.Glycaemia;
import org.project.adam.persistence.Meal;

import java.util.Calendar;
import java.util.Date;

public class DatabasePopulator {

    private static void createDiet(AppDatabase db, Diet diet, Meal... meals) {
        db.beginTransaction();
        try {
            db.dietDao().insert(diet);
            db.mealDao().insert(meals);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    private static void insertGlycaemia(AppDatabase db,
                                        int glyceariaId,
                                        int lunchId,
                                        float value,
                                        Date date,
                                        String context,
                                        String comment) {
        createGlycaemia(db, Glycaemia.builder()
            .id(glyceariaId)
            .value(value)
            .date(date)
            .context(context)
            .comment(comment)
            .build());

    }

    private static void createGlycaemia(AppDatabase db, Glycaemia glycaemia) {
        db.beginTransaction();
        try {
            db.glycemiaDao().insert(glycaemia);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public static void insertSampleGlycaemia(AppDatabase db) {
        int id = 0;
        float value = 40f;

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);

        for (int i = 0; i < 5; ++i) {
            calendar.set(Calendar.HOUR_OF_DAY, i);
            insertGlycaemia(db, id++, 1, value, calendar.getTime(), "context " + id, "comment " + id);
            value += 10;
        }

        calendar.set(Calendar.DAY_OF_MONTH, 1);

        for (int i = 5; i < 12; ++i) {
            calendar.set(Calendar.HOUR_OF_DAY, i);
            insertGlycaemia(db, id++, 1, value, calendar.getTime(), "context " + id, "comment " + id);
            value += 10;
        }
    }

    public static void insertSampleDiets(AppDatabase db) {
        int baseLunchId = 0;
        int dietId = 1;
        createDiet(db, Diet.builder()
                .id(dietId)
                .name("Régime 1")
                .build(),
            Meal.builder()
                .id(baseLunchId++)
                .dietId(dietId)
                .content("Oeufs")
                .timeOfDay(525)
                .build(),
            Meal.builder()
                .id(baseLunchId++)
                .dietId(dietId)
                .content("Maizena")
                .timeOfDay(645)
                .build(),
            Meal.builder()
                .id(baseLunchId++)
                .dietId(dietId)
                .content("Riz & Poisson")
                .timeOfDay(765)
                .build(),
            Meal.builder()
                .id(baseLunchId++)
                .dietId(dietId)
                .content("Maizena")
                .timeOfDay(885)
                .build(),
            Meal.builder()
                .id(baseLunchId++)
                .dietId(dietId)
                .content("Maizena")
                .timeOfDay(1025)
                .build(),
            Meal.builder()
                .id(baseLunchId++)
                .dietId(dietId)
                .content("Pate")
                .timeOfDay(1200)
                .build(),
            Meal.builder()
                .id(baseLunchId++)
                .dietId(dietId)
                .content("Tete de veau")
                .timeOfDay(1310)
                .build());

        dietId = 2;
        createDiet(db, Diet.builder()
                .id(dietId)
                .name("Régime 2")
                .build(),
            Meal.builder()
                .id(baseLunchId++)
                .dietId(dietId)
                .content("Oeufs")
                .timeOfDay(0)
                .build(),
            Meal.builder()
                .id(baseLunchId++)
                .dietId(dietId)
                .content("Maizena")
                .timeOfDay(645)
                .build(),
            Meal.builder()
                .id(baseLunchId++)
                .dietId(dietId)
                .content("Riz & Poisson")
                .timeOfDay(765)
                .build(),
            Meal.builder()
                .id(baseLunchId++)
                .dietId(dietId)
                .content("Maizena")
                .timeOfDay(885)
                .build(),
            Meal.builder()
                .id(baseLunchId++)
                .dietId(dietId)
                .content("Maizena")
                .timeOfDay(1025)
                .build(),
            Meal.builder()
                .id(baseLunchId++)
                .dietId(dietId)
                .content("Pate")
                .timeOfDay(1200)
                .build(),
            Meal.builder()
                .id(baseLunchId++)
                .dietId(dietId)
                .content("Tete de veau")
                .timeOfDay(1310)
                .build());
    }


}
