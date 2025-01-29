package com.example.eventplanner.settings;

import static android.content.Context.MODE_PRIVATE;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.example.eventplanner.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    private PreferenceManager prefManager;

    private static SettingsFragment newInstance(){
        Bundle args = new Bundle();
        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        prefManager = getPreferenceManager();
        prefManager.setSharedPreferencesName("pref_file");
        prefManager.setSharedPreferencesMode(MODE_PRIVATE);
        addPreferencesFromResource(R.xml.root_preferences);
    }
}