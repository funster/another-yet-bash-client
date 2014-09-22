package ru.aim.anotheryetbashclient.helper;

import android.util.Log;

import ru.aim.anotheryetbashclient.BuildConfig;

public final class L {

    private static boolean isDebug = BuildConfig.DEBUG;

    private L() {
        throw new AssertionError();
    }

    public static void d(String tag, String text, Throwable throwable) {
        if (isDebug) {
            Log.d(tag, text, throwable);
        }
    }

    public static void d(String tag, String text) {
        if (isDebug) {
            Log.d(tag, text);
        }
    }

    public static void e(String tag, String text, Throwable throwable) {
        if (isDebug) {
            Log.e(tag, text, throwable);
        }
    }

    public static void w(String tag, String text) {
        if (isDebug) {
            Log.w(tag, text);
        }
    }
}
