package ru.aim.anotheryetbashclient;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

/**
 *
 */
public class SettingsActivity extends ActionBarActivity {

    public static final String LIST_ITEM_ANIMATION = "enable_list_item_animation";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_container);

        getFragmentManager().beginTransaction().
                add(R.id.container, new SettingsFragment())
                .commit();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            addPreferencesFromResource(R.xml.pref_update);
            addPreferencesFromResource(R.xml.pref_about);

            CheckBoxPreference enableUpdatePreference = (CheckBoxPreference) findPreference(getString(R.string.auto_update_enable_key));
            enableUpdatePreference.setOnPreferenceChangeListener(this);

            TimePreference timePreference = (TimePreference) findPreference(getString(R.string.auto_update_time_key));
            timePreference.setSummary(getString(R.string.auto_update_summary, SettingsHelper.getUpdateTime(getActivity())));

            NumberPickerPreference numberPickerPreference = (NumberPickerPreference) findPreference(getString(R.string.auto_update_depth_key));
            numberPickerPreference.setSummary(getString(R.string.auto_update_depth_summary, SettingsHelper.getOfflinePages(getActivity())));

            Preference versionPreference = findPreference(getString(R.string.version_key));
            try {
                String versionName = getActivity().getPackageManager()
                        .getPackageInfo(getActivity().getPackageName(), 0).versionName;
                if (BuildConfig.DEBUG) {
                    versionName += " (" + getActivity().getPackageManager().
                            getPackageInfo(getActivity().getPackageName(), 0).versionCode + ")";
                }
                versionPreference.setSummary(getString(R.string.version_summary, versionName));
                versionPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        AboutDialog aboutDialog = new AboutDialog();
                        aboutDialog.show(getFragmentManager(), "about-dialog");
                        return true;
                    }
                });
            } catch (PackageManager.NameNotFoundException ignored) {
            }
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            boolean value = (Boolean) newValue;
            if (value) {
                AlarmReceiver.setAlarm(getActivity());
            } else {
                AlarmReceiver.cancelAlarm(getActivity());
            }
            return true;
        }
    }
}