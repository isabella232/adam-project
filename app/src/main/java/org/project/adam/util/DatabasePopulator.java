package org.project.adam.util;


import org.project.adam.AppDatabase;
import org.project.adam.persistence.Diet;
import org.project.adam.persistence.Glycaemia;
import org.project.adam.persistence.Lunch;

import java.util.Date;

public class DatabasePopulator {

    public static void initializeDb(AppDatabase db) {
        insertDiet(db, 1, 0, "Premier régime");
        // Dirty
        insertDiet(db, 2, 7, "Second régime");
        // Dirty
        insertDiet(db, 3, 12, "Third menu");
        insertManyGlycaemia(db);
        insertDiet(db, 3, 14, "Third régime");
    }

    private static void insertDiet(AppDatabase db, int dietId, int baseLunchId, String menuName) {
        createDiet(db, Diet.builder()
                .id(dietId)
                .name(menuName)
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
                .build(),
            Lunch.builder()
                .id(baseLunchId + 7)
                .dietId(dietId)
                .content("Tete de veau")
                .timeOfDay(1310)
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

    private static void insertManyGlycaemia(AppDatabase db) {
        int id = 0;
        float value = 0.5f;
        for (int i = 0; i < 5; ++i) {
            insertGlycaemia(db, id++, 1, value, new Date(), "context " + id, "comment " + id);
            value += 0.1;
        }
    }

    private static void insertGlycaemia(AppDatabase db,
                                        int glyceariaId,
                                        int lunchId,
                                        float value,
                                        Date date,
                                        String context,
                                        String comment
    ) {
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
}
