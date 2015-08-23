package ru.aim.anotheryetbashclient;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

public class PermissionsUtil {

    public static final int EXTERNAL_STORAGE_REQUEST = 56;
    public static final String EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";
    public static final String INTERNET = "android.permission.INTERNET";
    public static final String NETWORK_STATE = "android.permission.ACCESS_NETWORK_STATE";

    public static boolean isWriteExternalAllowed(Context context) {
        return isAllowed(context, EXTERNAL_STORAGE);
    }

    public static boolean isInternetAllowed(Context context) {
        return isAllowed(context, INTERNET);
    }

    public static boolean isAllowed(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

}
