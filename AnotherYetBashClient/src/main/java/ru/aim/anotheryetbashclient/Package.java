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

    static void updateHeader(Activity activity, int currentType, int page) {
        String[] types = activity.getResources().getStringArray(R.array.types);
        String actionBarTitle;
        if (currentType == ActionsAndIntents.TYPE_NEW && page > 0) {
            actionBarTitle = activity.getResources().getString(R.string.app_name_with_type_and_page, types[currentType], Integer.toString(page));
        } else {
            actionBarTitle = activity.getResources().getString(R.string.app_name_with_type, types[currentType]);
        }
        assert activity.getActionBar() != null;
        activity.getActionBar().setTitle(actionBarTitle);
    }
}
