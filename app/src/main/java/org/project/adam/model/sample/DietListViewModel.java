package org.project.adam.model.sample;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import org.project.adam.AppDatabase;
import org.project.adam.model.Diet;

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


    public void addItem(final Diet diet) {

        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                appDatabase.dietDao().insert(diet);
                return null;
            }
        }.execute();

    }
}