package org.project.adam.util;
/**
 * Adam project
 * Copyright (C) 2017 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.project.adam.AppDatabase;
import org.project.adam.persistence.Diet;
import org.project.adam.persistence.Glycaemia;
import org.project.adam.persistence.Meal;

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

    private static void insertGlycaemia(AppDatabase db, int glycaemia, float value, LocalDateTime date,
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

        LocalDateTime today = LocalDateTime.now()
            .withTime(0,0,0,0);

        for (int i = 0; i < 24; i++) {
            insertGlycaemia(db, id++, value, today.plusHours(i), "context " + id, "comment " + id);
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
                .timeOfDay(new LocalTime(8,45))
                .build(),
            Meal.builder()
                .id(baseLunchId++)
                .dietId(dietId)
                .content("Maizena")
                .timeOfDay(new LocalTime(10,45))
                .build(),
            Meal.builder()
                .id(baseLunchId++)
                .dietId(dietId)
                .content("Riz & Poisson")
                .timeOfDay(new LocalTime(12,45))
                .build(),
            Meal.builder()
                .id(baseLunchId++)
                .dietId(dietId)
                .content("Maizena")
                .timeOfDay(new LocalTime(14,45))
                .build(),
            Meal.builder()
                .id(baseLunchId++)
                .dietId(dietId)
                .content("Maizena")
                .timeOfDay(new LocalTime(17,05))
                .build(),
            Meal.builder()
                .id(baseLunchId++)
                .dietId(dietId)
                .content("Pate")
                .timeOfDay(new LocalTime(20,0))
                .build(),
            Meal.builder()
                .id(baseLunchId++)
                .dietId(dietId)
                .content("Tete de veau")
                .timeOfDay(new LocalTime(21,50))
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
                .timeOfDay(new LocalTime(8,45))
                .build(),
            Meal.builder()
                .id(baseLunchId++)
                .dietId(dietId)
                .content("35g de Maizena\n" +
                    "10 g de Maxijul OU maltodextridine")
                .timeOfDay(new LocalTime(10,30))
                .build(),
            Meal.builder()
                .id(baseLunchId++)
                .dietId(dietId)
                .content("130 g de féculents (pâtes cuites ou riz cuit)\n" +
                    "40 g de légumes mixes à l'eau\n" +
                    "50g de viande ou poisson par jour\n" +
                    "beurre ou huile +/- épices\n" +
                    "+/- Yaourt au soja")
                .timeOfDay(new LocalTime(12,0))
                .build(),
            Meal.builder()
                .id(baseLunchId++)
                .dietId(dietId)
                .content("Compléter par une prise de Maizena\n" +
                    "25g de Maizena\n" +
                    "5 g de Maltodextridine OU Maxijul")
                .timeOfDay(new LocalTime(13,30))
                .build(),
            Meal.builder()
                .id(baseLunchId++)
                .dietId(dietId)
                .content("35g de Maizena\n" +
                    "10 g de Maltodextridine OU Maxijul")
                .timeOfDay(new LocalTime(15,0))
                .build(),
            Meal.builder()
                .id(baseLunchId++)
                .dietId(dietId)
                .content("130 g de féculents (pâtes cuites ou riz cuit)\n" +
                    "40 g de légumes mixés à l'eau\n" +
                    "beurre ou huile +/- épices\n" +
                    "+/- Yaourt au soja")
                .timeOfDay(new LocalTime(18,0))
                .build(),
            Meal.builder()
                .id(baseLunchId++)
                .dietId(dietId)
                .content("35g de Maizena\n" +
                    "10 g de Maltodextridine OU Maxijul")
                .timeOfDay(new LocalTime(20,0))
                .build()
        );
    }


}
