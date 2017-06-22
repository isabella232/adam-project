package org.project.adam.ui.dashboard.lunch;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import org.project.adam.AppDatabase;
import org.project.adam.persistence.Lunch;

import java.util.List;

/**
 * Created by bastien on 22/06/2017.
 */

public class LunchListViewModel extends AndroidViewModel {

    final AppDatabase appDatabase;

    private LiveData<List<Lunch>> lunches = new MutableLiveData<>();

    public LunchListViewModel(Application application) {
        super(application);
        appDatabase = AppDatabase.getDatabase(application);
    }

    public LiveData<List<Lunch>> findFromDiet(int dietId) {
        lunches = appDatabase.lunchDao().findFromDiet(dietId);
        return lunches;
    }
}
