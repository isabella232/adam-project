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

    private static void insertGlycaemia(AppDatabase db, int glycaemia, float value, Date date,
                                        String context, String comment) {
        createGlycaemia(db, Glycaemia.builder()
            .id(glycaemia)
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

        for (int i = 0; i < 24; i++) {
            calendar.set(Calendar.HOUR_OF_DAY, i);
            insertGlycaemia(db, id++, value, calendar.getTime(), "context " + id, "comment " + id);
            value = i < 12 ? value + 5 : value - 5;
        }

    }

    public static void insertSampleDiets(AppDatabase db) {
        int baseLunchId = 0;
        int dietId = 1;
        createDiet(db, Diet.builder()
                .id(dietId)
                .name("Monoligne")
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
                .name("Multilignes")
                .build(),
            Meal.builder()
                .id(baseLunchId++)
                .dietId(dietId)
                .content("30g de pain complet\n" +
                    "huile d'olive")
                .timeOfDay(8 * 60 + 45)
                .build(),
            Meal.builder()
                .id(baseLunchId++)
                .dietId(dietId)
                .content("35g de Maizena\n" +
                    "10 g de Maxijul OU maltodextridine")
                .timeOfDay(10 * 60 + 30)
                .build(),
            Meal.builder()
                .id(baseLunchId++)
                .dietId(dietId)
                .content("130 g de féculents (pâtes cuites ou riz cuit)\n" +
                    "40 g de légumes mixes à l'eau\n" +
                    "50g de viande ou poisson par jour\n" +
                    "beurre ou huile +/- épices\n" +
                    "+/- Yaourt au soja")
                .timeOfDay(12 * 60)
                .build(),
            Meal.builder()
                .id(baseLunchId++)
                .dietId(dietId)
                .content("Compléter par une prise de Maizena\n" +
                    "25g de Maizena\n" +
                    "5 g de Maltodextridine OU Maxijul")
                .timeOfDay(13 * 60 + 30)
                .build(),
            Meal.builder()
                .id(baseLunchId++)
                .dietId(dietId)
                .content("35g de Maizena\n" +
                    "10 g de Maltodextridine OU Maxijul")
                .timeOfDay(15 * 60)
                .build(),
            Meal.builder()
                .id(baseLunchId++)
                .dietId(dietId)
                .content("130 g de féculents (pâtes cuites ou riz cuit)\n" +
                    "40 g de légumes mixés à l'eau\n" +
                    "beurre ou huile +/- épices\n" +
                    "+/- Yaourt au soja")
                .timeOfDay(18 * 60)
                .build(),
            Meal.builder()
                .id(baseLunchId++)
                .dietId(dietId)
                .content("35g de Maizena\n" +
                    "10 g de Maltodextridine OU Maxijul")
                .timeOfDay(20 * 60)
                .build()
        );
    }


}
