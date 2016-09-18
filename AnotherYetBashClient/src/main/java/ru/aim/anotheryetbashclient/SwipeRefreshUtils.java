package ru.aim.anotheryetbashclient;

import android.support.v4.widget.SwipeRefreshLayout;

import static ru.aim.anotheryetbashclient.AttrUtils.resolveResource;

/**
 *
 */
public class SwipeRefreshUtils {

    public static void applyStyle(SwipeRefreshLayout refreshLayout) {
        int color = resolveResource(refreshLayout.getContext(), R.attr.colorPrimary);
        if (color != 0) {
            refreshLayout.setColorSchemeResources(
                    color, color, color, color);
        }
    }
}
