package org.project.adam.ui.preferences;
/**
 * Adam project
 * Copyright (C) 2017 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.preference.RingtonePreference;
import android.text.InputType;

import org.androidannotations.annotations.AfterPreferences;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.PreferenceByKey;
import org.androidannotations.annotations.PreferenceChange;
import org.androidannotations.annotations.PreferenceScreen;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.project.adam.Preferences_;
import org.project.adam.R;
import org.project.adam.alert.AlertScheduler;

import java.util.Locale;

import timber.log.Timber;

@SuppressLint("Registered")
@PreferenceScreen(R.xml.settings)
@OptionsMenu(R.menu.main)
@EFragment
public class PrefFragment extends PreferenceFragment {

    @StringRes(R.string.pref_alert_type_value_notif)
    protected String alertTypeNotif;

    @StringRes(R.string.pref_alert_type_value_alarm)
    protected String alertTypeAlarm;


    @PreferenceByKey(R.string.pref_time_before_alert)
    EditTextPreference timeAlertPreference;

    @PreferenceByKey(R.string.pref_recipients)
    EditTextPreference recipientsPreference;

    @PreferenceByKey(R.string.pref_notif)
    Preference notifPreference;

    @PreferenceByKey(R.string.pref_min_glycaemia)
    EditTextPreference minGlycaemiaPreference;

    @PreferenceByKey(R.string.pref_max_glycaemia)
    EditTextPreference maxGlycaemiaPreference;

    @PreferenceByKey(R.string.pref_risk_glycaemia)
    EditTextPreference riskGlycaemiaPreference;

    @PreferenceByKey(R.string.pref_alert_type)
    ListPreference alertType;

    @PreferenceByKey(R.string.pref_alarm_sound)
    RingtonePreference alarmRingtonePref;

    @PreferenceByKey(R.string.pref_notif)
    RingtonePreference notifRingtonePref;

    @Pref
    protected Preferences_ prefs;

    @StringRes(R.string.pref_default_time_before_alert)
    protected String defaultTime;

    @Bean
    protected AlertScheduler alertScheduler;

    @AfterPreferences
    void initPrefs() {
        timeAlertPreference.setText(prefs.reminderTimeInMinutes().getOr(Integer.valueOf(defaultTime)) + "");
        timeAlertPreference.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);

        minGlycaemiaPreference.setText(prefs.minGly().get() + "");
        minGlycaemiaPreference.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
        maxGlycaemiaPreference.setText(prefs.maxGly().get() + "");
        maxGlycaemiaPreference.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
        riskGlycaemiaPreference.setText(prefs.riskGly().get() + "");
        riskGlycaemiaPreference.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);


        recipientsPreference.setText(prefs.recipientsEmails().getOr(""));
        recipientsPreference.getEditText().setSingleLine(false);
        recipientsPreference.setSummary(prefs.recipientsEmails().getOr(""));

        //alertType.setValueIndex(alertType.findfindIndexOfValue(prefs.alertType().get()));
        updateVisibilityAccordingToAlertType(prefs.alertType().get());

        initSummary(getPreferenceScreen());

        if (ActivityManager.isUserAMonkey()) {
            getActivity().finish();
        }


    }

    @PreferenceChange(R.string.pref_time_before_alert)
    void timeBeforeAlertChanged(Preference preference, String newValue) {
        prefs.reminderTimeInMinutes().put(Integer.valueOf(newValue));
        setEditTextSummary(preference, newValue);
        alertScheduler.schedule();
    }

    @PreferenceChange(R.string.pref_recipients)
    void recipientsChanged(Preference preference, String newValue) {
        prefs.recipientsEmails().put(newValue);
        setEditTextSummary(preference, newValue);
    }

    @PreferenceChange(R.string.pref_min_glycaemia)
    void minGlycaemiaChanged(Preference preference, String newValue) {
        prefs.minGly().put(Integer.valueOf(newValue));
        setEditTextSummary(preference, newValue);
    }

    @PreferenceChange(R.string.pref_max_glycaemia)
    void maxGlycaemiaChanged(Preference preference, String newValue) {
        prefs.maxGly().put(Integer.valueOf(newValue));
        setEditTextSummary(preference, newValue);
    }

    @PreferenceChange(R.string.pref_risk_glycaemia)
    void riskGlycaemiaChanged(Preference preference, String newValue) {
        prefs.riskGly().put(Integer.valueOf(newValue));
        setEditTextSummary(preference, newValue);
    }

    @PreferenceChange(R.string.pref_notif)
    void notifSoundChanged(Preference preference, String newValue) {
        prefs.notifRingtone().put(newValue);
    }

    @PreferenceChange(R.string.pref_alarm_sound)
    void alarmSoundChanged(Preference preference, String newValue) {
        prefs.alarmRingtone().put(newValue);
    }

    @PreferenceChange(R.string.pref_alert_type)
    void alertTypeChanged(ListPreference preference, String newValue) {
        Timber.d(newValue);
        prefs.alertType().put(newValue);
        updateVisibilityAccordingToAlertType(newValue);
        preference.setSummary(preference.getEntries()[preference.findIndexOfValue(newValue)]);
    }

    private void initSummary(Preference p) {
        if (p instanceof PreferenceGroup) {
            PreferenceGroup pGrp = (PreferenceGroup) p;
            for (int i = 0; i < pGrp.getPreferenceCount(); i++) {
                initSummary(pGrp.getPreference(i));
            }
        } else {
            updatePrefSummary(p);
        }
    }

    private void updateVisibilityAccordingToAlertType(String value) {
        notifRingtonePref.setEnabled(value.equals(alertTypeNotif));
        alarmRingtonePref.setEnabled(value.equals(alertTypeAlarm));
    }

    private void updatePrefSummary(Preference p) {
        if (p instanceof ListPreference) {
            ListPreference listPref = (ListPreference) p;
            p.setSummary(listPref.getEntry());
        }
        if (p instanceof EditTextPreference) {
            setEditTextSummary(p, null);
        }
    }

    private void setEditTextSummary(Preference p, String newValue) {

        EditTextPreference editTextPref = (EditTextPreference) p;
        if (newValue == null) {
            newValue = editTextPref.getText();
        }
        if (p.getTitle().toString().toLowerCase(Locale.getDefault()).contains("password")) {
            p.setSummary("******");
        } else {
            p.setSummary(newValue);
        }
    }


}
