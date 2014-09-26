package ru.aim.anotheryetbashclient;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
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

    public static int fontSize(Context context) {
        int value;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        TypedArray a = context.getTheme().obtainStyledAttributes(android.R.style.TextAppearance_Medium, new int[]{android.R.attr.textSize});
        value = toSp(a.getString(0));
        a.recycle();
        return preferences.getInt("font_size", value);
    }

    static int toSp(String spText) {
        return Integer.parseInt(spText.replace(".0sp", ""));
    }
}
