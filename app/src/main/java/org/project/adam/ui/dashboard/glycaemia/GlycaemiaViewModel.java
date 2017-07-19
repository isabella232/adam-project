package org.project.adam.ui.dashboard.glycaemia;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import org.joda.time.LocalDateTime;
import org.project.adam.AppDatabase;
import org.project.adam.persistence.Glycaemia;

import java.util.List;

public class GlycaemiaViewModel extends AndroidViewModel {

    final AppDatabase appDatabase;

    private LiveData<List<Glycaemia>> glycaemias = new MutableLiveData<>();

    public GlycaemiaViewModel(Application application) {
        super(application);
        appDatabase = AppDatabase.getDatabase(application);
    }

    public LiveData<List<Glycaemia>> findGlycaemiaBetween(LocalDateTime min, LocalDateTime max) {
        glycaemias = appDatabase.glycemiaDao().findGlycaemiaBetween(min, max);
        return glycaemias;
    }


}
