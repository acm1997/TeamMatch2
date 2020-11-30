package com.example.teammatch.fragments;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.example.teammatch.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    public static final String KEY_PREF_NIGHTMODE = "pref_nightmode";
    public static final String KEY_PREF_USERNAME = "pref_username";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        addPreferencesFromResource(R.xml.root_preferences);

    }
}