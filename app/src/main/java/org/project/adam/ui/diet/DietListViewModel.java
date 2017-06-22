package org.project.adam.ui.diet;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import org.project.adam.AppDatabase;
import org.project.adam.persistence.Diet;
import org.project.adam.persistence.Lunch;

import java.util.List;

import lombok.Getter;
import timber.log.Timber;

public class DietListViewModel extends AndroidViewModel {

    final AppDatabase appDatabase;

    @Getter
    private LiveData<List<Diet>> diets;

    public DietListViewModel(Application application) {
        super(application);

        appDatabase = AppDatabase.getDatabase(application);
        diets = appDatabase.dietDao().findAll();
    }

    public void createDiet(final Diet diet, final Lunch ... lunches){
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                List<Long> generatedIds = appDatabase.dietDao().insert(diet);
                Timber.w("Diet %d", generatedIds.size());
                if(generatedIds.size() == 1
                    && generatedIds.get(0) != null && generatedIds.get(0) != 0){
                    for(Lunch lunch : lunches){
                        lunch.setDietId(generatedIds.get(0).intValue());
                    }
                    appDatabase.lunchDao().insert(lunches);
                }
                return null;
            }
        }.execute();
    }

}