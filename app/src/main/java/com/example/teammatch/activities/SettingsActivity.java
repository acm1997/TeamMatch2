package com.example.teammatch.activities;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.teammatch.R;

public class SettingsActivity extends AppCompatActivity {

  //  private CheckBoxPreference nightmode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
                setPreferencesFromResource(R.xml.root_preferences, rootKey);
                SharedPreferences preferences;
            final Preference nightmode = (Preference) findPreference("pref_nightmode");
            preferences = this.getActivity().getSharedPreferences("Preferences", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            boolean isNightModeOn = preferences.getBoolean("pref_nightmode", false);

            nightmode.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    if(isNightModeOn){
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        editor.putBoolean("pref_nightmode", false);
                        editor.apply();
                        return true;
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        editor.putBoolean("pref_nightmode", true);
                        editor.apply();
                        return true;
                    }
                }
            });
        }
    }
}