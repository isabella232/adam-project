package org.project.adam.ui.dashboard.meal;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import org.project.adam.AppDatabase;
import org.project.adam.persistence.Meal;

import java.util.List;

/**
 * Created by bastien on 22/06/2017.
 */

public class MealListViewModel extends AndroidViewModel {

    final AppDatabase appDatabase;

    private LiveData<List<Meal>> meals = new MutableLiveData<>();

    public MealListViewModel(Application application) {
        super(application);
        appDatabase = AppDatabase.getDatabase(application);
    }

    public LiveData<List<Meal>> findFromDiet(int dietId) {
        meals = appDatabase.mealDao().findFromDiet(dietId);
        return meals;
    }
}
