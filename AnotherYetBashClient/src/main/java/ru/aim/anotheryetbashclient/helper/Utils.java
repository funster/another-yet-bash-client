package ru.aim.anotheryetbashclient.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.AndroidHttpClient;
import android.os.Build;

import org.apache.http.client.methods.HttpGet;

import java.io.IOException;

public final class Utils {

    static final String TAG = "Utils";

    private Utils() {
        throw new AssertionError();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean contains(long[] arr, long value) {
        //noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == value) {
                return true;
            }
        }
        return false;
    }

    public static boolean ping(Context context) {
        AndroidHttpClient client = AndroidHttpClient.newInstance(Build.DEVICE);
        HttpGet httpGet = new HttpGet("http:\\google.com");
        Exception error = null;
        try {
            client.execute(httpGet);
        } catch (IOException e) {
            L.e(TAG, "Error while trying to ping google", e);
            error = e;
        }
        return error == null;
    }
}
