package ru.aim.anotheryetbashclient;

import android.support.v4.widget.SwipeRefreshLayout;

/**
 *
 */
public class SwipeRefreshUtils {

    public static void applyStyle(SwipeRefreshLayout refreshLayout) {
        refreshLayout.setColorSchemeResources(
                R.color.app_colorPrimary,
                R.color.app_colorPrimary,
                R.color.app_colorPrimary,
                R.color.app_colorPrimary);
    }
}
