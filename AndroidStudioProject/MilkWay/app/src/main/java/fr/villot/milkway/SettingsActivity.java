package fr.villot.milkway;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class SettingsActivity extends PreferenceActivity
{
    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }

    public static class MyPreferenceFragment extends PreferenceFragment
    {

        public static final String PREF_IP_ADDRESS = "ipAdress";
        public static final String PREF_PORT = "portNumber";
        public static final String PREF_TIMEOUT = "timeout";
        public static final String PREF_NB_RELAY = "nbRelay";

        private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;

        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preference);

            final SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
            preferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

//                if (key.equals(PREF_IP_ADRESS)) {
                    Preference pref = findPreference(key);
                    pref.setSummary(sharedPreferences.getString(key, ""));
//                }
                }
            };
        }

        @Override
        public void onResume() {
            super.onResume();

            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(preferenceChangeListener);

            Preference prefIpAdress = findPreference(PREF_IP_ADDRESS);
            prefIpAdress.setSummary(getPreferenceScreen().getSharedPreferences().getString(PREF_IP_ADDRESS, ""));

            Preference prefPort = findPreference(PREF_PORT);
            prefPort.setSummary(getPreferenceScreen().getSharedPreferences().getString(PREF_PORT, ""));

            Preference prefTimeout = findPreference(PREF_TIMEOUT);
            prefTimeout.setSummary(getPreferenceScreen().getSharedPreferences().getString(PREF_TIMEOUT, ""));

            Preference prefNbRelay = findPreference(PREF_NB_RELAY);
            prefNbRelay.setSummary(getPreferenceScreen().getSharedPreferences().getString(PREF_NB_RELAY, ""));
        }

        @Override
        public void onPause() {
            getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);

            super.onPause();
        }


    }
}