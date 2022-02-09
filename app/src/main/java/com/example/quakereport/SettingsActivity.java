package com.example.quakereport;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

    }

    public static class EarthquakePreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener{

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);

            Preference minMagnitude = findPreference(getString(R.string.settings_min_magnitude_key));
            bindPreferenceSummaryToValue(minMagnitude);

            Preference maxMagnitude = findPreference(getString(R.string.settings_max_magnitude_key));
            bindPreferenceSummaryToValue(maxMagnitude);

            Preference itemNumber = findPreference(getString(R.string.settings_item_number_key));
            bindPreferenceSummaryToValue(itemNumber);

            Preference orderBy = findPreference(getString(R.string.settings_order_by_key));
            bindPreferenceSummaryToValue(orderBy);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();
            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int prefIndex = listPreference.findIndexOfValue(stringValue);
                if (prefIndex >= 0) {
                    CharSequence[] labels = listPreference.getEntries();
                    preference.setSummary(labels[prefIndex]);
                }
            }
            else if (preference instanceof EditTextPreference){
                if (preference.getKey().equals(getString(R.string.settings_item_number_key))){
                    if (Integer.parseInt(stringValue) > 0 && Integer.parseInt(stringValue) <= 100){
                        preference.setSummary(stringValue);
                    }
                    else if (Integer.parseInt(stringValue) <= 0){
                        preference.setSummary("1");
                    }
                    else{
                        preference.setSummary("100");
                    }
                }
                else {
                    preference.setSummary(stringValue);
                }
            }
            return true;
        }

        private void bindPreferenceSummaryToValue(Preference preference) {
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferenceString = preferences.getString(preference.getKey(), "");
            onPreferenceChange(preference, preferenceString);
        }


    }
}