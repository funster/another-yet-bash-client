package ru.aim.anotheryetbashclient;

import android.support.v4.widget.SwipeRefreshLayout;

/**
 *
 */
public class SwipeRefreshUtils {

    public static void applyStyle(SwipeRefreshLayout refreshLayout) {
        refreshLayout.setColorSchemeResources(
                android.R.color.white,
                android.R.color.holo_orange_dark,
                android.R.color.holo_red_dark,
                android.R.color.darker_gray);
    }
}
