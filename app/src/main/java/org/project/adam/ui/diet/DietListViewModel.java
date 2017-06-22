package org.project.adam.ui.diet;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import org.project.adam.AppDatabase;
import org.project.adam.persistence.Diet;

import java.util.List;

import lombok.Getter;

public class DietListViewModel extends AndroidViewModel {

    final AppDatabase appDatabase;

    @Getter
    private LiveData<List<Diet>> diets;

    public DietListViewModel(Application application) {
        super(application);

        appDatabase = AppDatabase.getDatabase(application);
        diets = appDatabase.dietDao().findAll();
    }

    public void removeDiet(final Diet diet) {

        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                appDatabase.dietDao().delete(diet);
                return null;
            }
        }.execute();

    }
}