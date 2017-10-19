package org.project.adam;
/**
 * Adam project
 * Copyright (C) 2017 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import android.annotation.SuppressLint;
import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.squareup.leakcanary.LeakCanary;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EApplication;
import org.project.adam.alert.AlertScheduler;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;


@SuppressLint("Registered")
@EApplication
public class App extends Application {

    @Bean
    AlertScheduler alertScheduler;

    @AfterInject
    public void initLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    @AfterInject
    public void initFabric() {
        CrashlyticsCore core = new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build();
        Fabric.with(this, new Crashlytics.Builder().core(core).build());
    }

    @AfterInject
    public void setUpAlarms() {
        alertScheduler.schedule();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }
}

