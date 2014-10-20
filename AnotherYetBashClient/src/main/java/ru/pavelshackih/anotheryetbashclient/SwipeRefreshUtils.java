package ru.pavelshackih.anotheryetbashclient;

import android.support.v4.widget.SwipeRefreshLayout;

/**
 *
 */
public class SwipeRefreshUtils {

    public static void applyStyle(SwipeRefreshLayout refreshLayout) {
        refreshLayout.setColorSchemeResources(
                R.color.default_app_color,
                R.color.default_app_color,
                R.color.default_app_color,
                R.color.default_app_color);
    }
}
