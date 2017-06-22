package org.project.adam.util;


import org.project.adam.AppDatabase;
import org.project.adam.model.Diet;
import org.project.adam.model.Lunch;

import java.util.List;

public class DatabasePopulator {

    public static void initializeDb(AppDatabase db) {
        insertDiet(db, 1, 0, "Premier menu", false);
        // Dirty
        insertDiet(db, 2, 6, "Second menu", true);
    }

    private static void insertDiet(AppDatabase db, int dietId, int baseLunchId, String menuName, boolean current) {
        createDiet(db, Diet.builder()
                .id(dietId)
                .name(menuName)
                .current(current)
                .build(),
            Lunch.builder()
                .id(baseLunchId + 1)
                .dietId(dietId)
                .content("Oeufs")
                .timeOfDay(525)
                .build(),
            Lunch.builder()
                .id(baseLunchId + 2)
                .dietId(dietId)
                .content("Maizena")
                .timeOfDay(645)
                .build(),
            Lunch.builder()
                .id(baseLunchId + 3)
                .dietId(dietId)
                .content("Riz & Poisson")
                .timeOfDay(765)
                .build(),
            Lunch.builder()
                .id(baseLunchId + 4)
                .dietId(dietId)
                .content("Maizena")
                .timeOfDay(885)
                .build(),
            Lunch.builder()
                .id(baseLunchId + 5)
                .dietId(dietId)
                .content("Maizena")
                .timeOfDay(1025)
                .build(),
            Lunch.builder()
                .id(baseLunchId + 6)
                .dietId(dietId)
                .content("Pate")
                .timeOfDay(1200)
                .build());
    }

    private static void createDiet(AppDatabase db, Diet diet, Lunch... lunches) {
        db.beginTransaction();
        try {
            db.dietDao().insert(diet);
            db.lunchDao().insert(lunches);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
}
