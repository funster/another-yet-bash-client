package ru.aim.anotheryetbashclient;

import android.app.Activity;

/**
 *
 */
final class Package {

    private Package() {
        throw new AssertionError();
    }

    static void updateHeader(Activity activity, int currentType) {
        updateHeader(activity, currentType, -1);
    }

    @SuppressWarnings("ConstantConditions")
    static void updateHeader(Activity activity, int currentType, int page) {
        String[] types = activity.getResources().getStringArray(R.array.types);
        String actionBarTitle;
        actionBarTitle = activity.getResources().getString(R.string.app_name_with_type, types[currentType]);
        activity.getActionBar().setTitle(actionBarTitle);
    }
}
