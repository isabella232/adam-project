package org.project.adam.persistence;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.project.adam.AppDatabase;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(AndroidJUnit4.class)
public class DietDaoTest {
    private DietDao dietDao;

    private AppDatabase database;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        dietDao = database.dietDao();
    }

    @After
    public void closeDb() {
        database.close();
    }

    @Test
    public void write_diet_should_return_generated_id() throws Exception {
        // Given
        Diet diet1 = Diet.builder()
                .name("Test diet 1")
                .build();
        Diet diet2 = Diet.builder()
            .name("Test diet 2")
            .build();
        // When
        List<Long> generatedIds = dietDao.insert(diet1, diet2);

        // Then
        assertThat(generatedIds).isNotNull();
        assertThat(generatedIds).hasSize(2);
        assertThat(generatedIds.get(0)).isNotZero();
        assertThat(generatedIds.get(1)).isNotZero();
        assertThat(generatedIds.get(0)).isNotEqualTo(generatedIds.get(1));
    }

}