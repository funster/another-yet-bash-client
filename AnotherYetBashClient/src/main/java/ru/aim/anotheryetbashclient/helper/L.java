package ru.aim.anotheryetbashclient.helper;

import android.util.Log;

public final class L {

    public static final boolean DEBUG = true;

    private L() {
        throw new AssertionError();
    }

    public static void d(String tag, String text, Throwable throwable) {
        if (DEBUG) {
            Log.d(tag, text, throwable);
        }
    }

    public static void d(String tag, String text) {
        if (DEBUG) {
            Log.d(tag, text);
        }
    }

    public static void e(String tag, String text, Throwable throwable) {
        if (DEBUG) {
            Log.e(tag, text, throwable);
        }
    }

    public static void w(String tag, String text) {
        if (DEBUG) {
            Log.w(tag, text);
        }
    }
}
