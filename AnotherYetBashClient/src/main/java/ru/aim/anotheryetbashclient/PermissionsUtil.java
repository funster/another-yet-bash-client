package ru.aim.anotheryetbashclient;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

public class PermissionsUtil {

    public static final int EXTERNAL_STORAGE_REQUEST = 56;

    public static boolean isWriteExternalAllowed(Context context) {
        return isAllowed(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    public static boolean isInternetAllowed(Context context) {
        return isAllowed(context, Manifest.permission.INTERNET);
    }

    public static boolean isAllowed(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }
}
