package ru.aim.anotheryetbashclient;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.preference.PreferenceManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import ru.aim.anotheryetbashclient.helper.DbHelper;

/**
 *
 */
public final class SettingsHelper {

    public static final String TYPE_ID = "typeId";

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm");

    private SettingsHelper() {
    }

    public static void saveType(Context context, int typeId) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(TYPE_ID, typeId);
        editor.apply();
    }

    public static int loadType(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(TYPE_ID, 0);
    }

    public static int getFontSize(Context context) {
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

    public static boolean isItemAnimationEnabled(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(SettingsActivity.LIST_ITEM_ANIMATION, true);
    }

    public static boolean isUpdateEnabled(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(context.getString(R.string.auto_update_enable_key), false);
    }

    public static boolean isPreloadedAvailable(Context context) {
        return isUpdateEnabled(context) && isFreshTableNotEmpty(context);
    }

    public static boolean isFreshTableNotEmpty(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        return !dbHelper.isEmptyFreshTable();
    }

    public static int getUpdateHour(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String hourString = preferences.getString(context.getString(R.string.auto_update_time_key), "7:00");
        return TimePreference.getHour(hourString);
    }

    public static int getUpdateMinute(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String hourString = preferences.getString(context.getString(R.string.auto_update_time_key), "7:00");
        return TimePreference.getMinute(hourString);
    }

    public static String getUpdateTime(Context context) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, getUpdateHour(context));
        calendar.set(Calendar.MINUTE, getUpdateMinute(context));
        return DATE_FORMAT.format(calendar.getTime());
    }

    public static void writeTimestamp(Context context, long timestamp) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (timestamp == 0) {
            preferences.edit().remove(context.getString(R.string.auto_update_timestamp_key)).clear();
        } else {
            preferences.edit().putLong(context.getString(R.string.auto_update_timestamp_key),
                    timestamp).apply();
        }
    }

    public static long getTimestamp(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getLong(context.getString(R.string.auto_update_timestamp_key), 0);
    }

    public static boolean isUpdateOnlyByWifi(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(context.getString(R.string.auto_update_wifi_key), true);
    }

    public static int getOfflinePages(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(context.getString(R.string.auto_update_depth_key), 1);
    }

    public static boolean isScrollByVolumeEnabled(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(context.getString(R.string.enable_scroll_volume), false);
    }
}
