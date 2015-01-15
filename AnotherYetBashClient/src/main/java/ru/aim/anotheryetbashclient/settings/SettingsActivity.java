package ru.aim.anotheryetbashclient.settings;

import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import ru.aim.anotheryetbashclient.AboutDialog;
import ru.aim.anotheryetbashclient.AlarmReceiver;
import ru.aim.anotheryetbashclient.R;
import ru.aim.anotheryetbashclient.support.ThemedActivity;

/**
 *
 */
public class SettingsActivity extends ThemedActivity {

    public static final String LIST_ITEM_ANIMATION = "enable_list_item_animation";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_container);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Fragment fragment = getFragmentManager().findFragmentByTag("container");
        if (fragment == null) {
            getFragmentManager().beginTransaction().
                    add(R.id.container, new SettingsFragment(), "container")
                    .commit();
        }

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
                versionName += " (" + getActivity().getPackageManager().
                        getPackageInfo(getActivity().getPackageName(), 0).versionCode + ")";
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

            final ListPreference listPreference = (ListPreference) findPreference(getString(R.string.select_theme_key));
            final String beforeValue = listPreference.getValue();
            listPreference.setSummary(beforeValue);
            listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    if (!beforeValue.equals(newValue)) {
                        if (getActivity() instanceof ThemedActivity) {
                            listPreference.setSummary((String) newValue);
                            ThemedActivity.sendChangeTheme(getActivity());
                        }
                    }
                    return true;
                }
            });

            CheckBoxPreference orientationChangePreference = (CheckBoxPreference) findPreference(getString(R.string.change_orientation_key));
            orientationChangePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    boolean boolValue = (Boolean) newValue;
                    SettingsActivity settingsActivity = (SettingsActivity) getActivity();
                    if (boolValue) {
                        settingsActivity.lockScreen();
                    } else {
                        settingsActivity.unlockScreen();
                    }
                    return true;
                }
            });
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