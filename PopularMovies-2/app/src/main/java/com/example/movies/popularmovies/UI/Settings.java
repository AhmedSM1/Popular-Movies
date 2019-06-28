package com.example.movies.popularmovies.UI;

import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.movies.popularmovies.R;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }
    public static class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            //First link this fragment with the PreferenceScreen
            addPreferencesFromResource(R.xml.settings);

            //Define the  Preference, set its summary, and set onChangeListener
            Preference sortByPreference = findPreference(getString(R.string.sort_by_key));
            setUpPreference(sortByPreference);

        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {

            ListPreference listPreference = (ListPreference) preference;
            String newSortBy = newValue.toString();

            //get the index of the selected value in the values array
            int index = listPreference.findIndexOfValue(newSortBy);

            //get the entry associated with the new value that was selected and set that as a summary
            if (index >= 0) {
                CharSequence[] entries = listPreference.getEntries();
                preference.setSummary(entries[index]);
            }
            return true;
        }


        private void setUpPreference(Preference preference) {
            /*Get the value saved in SharedPreferences for this preference
            Then use that value to pass it to the onPreferenceChangeListener of the preference
            inside onPreference we'll set the summary since every time the preference changes the summary should change*/

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());

            String selectedSortValue = sharedPreferences.getString(preference.getKey(), getString(R.string.sort_popularity));

            //set the onChangeListener for this preference
            preference.setOnPreferenceChangeListener(this);

            //call onPreferenceChange callback so we can set the initial summary of the Preference (When we first open the app)
            onPreferenceChange(preference, selectedSortValue);
        }

    }

}





