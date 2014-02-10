package ru.aim.anotheryetbashclient;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 *
 */
class SettingsHelper {

    private SettingsHelper() {
    }

    public static void saveType(Context context, int typeId) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("typeId", typeId);
        editor.commit();
    }

    public static int loadType(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt("typeId", 0);
    }
}
