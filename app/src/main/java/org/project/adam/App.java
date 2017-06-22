package org.project.adam;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EApplication;
import org.project.adam.util.DatabasePopulator;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;


@SuppressLint("Registered")
@EApplication
public class App extends Application {

    @AfterInject
    public void initLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                DatabasePopulator.initializeDb(AppDatabase.getDatabase(App.this));
                return null;
            }
        }.execute();

    }

    @AfterInject
    public void initFabric() {
        CrashlyticsCore core = new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build();
        Fabric.with(this, new Crashlytics.Builder().core(core).build());
    }
}
