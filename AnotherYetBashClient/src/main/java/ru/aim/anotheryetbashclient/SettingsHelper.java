package ru.aim.anotheryetbashclient;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 *
 */
public class SettingsHelper {

    public static final String TYPE_ID = "typeId";

    private SettingsHelper() {
    }

    public static void saveType(Context context, int typeId) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(TYPE_ID, typeId);
        editor.commit();
    }

    public static int loadType(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(TYPE_ID, 0);
    }
}
