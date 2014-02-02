package ru.aim.anotheryetbashclient.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.AndroidHttpClient;
import android.os.Build;
import org.apache.http.client.methods.HttpGet;
import ru.aim.anotheryetbashclient.helper.f.Block;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@SuppressWarnings("unused")
public final class Utils {

    static final String TAG = "Utils";

    public static final String UTF_8 = "UTF-8";
    public static final String WINDOWS_1215 = "windows-1251";

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

    public static void rethrowWithRuntime(Block block) {
        try {
            block.apply();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String readFromStream(InputStream is) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line = reader.readLine();
            StringBuilder sb = new StringBuilder();
            while (line != null) {
                sb.append(line);
                line = reader.readLine();
            }
            return sb.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static int[] toPrimitiveArray(List<Integer> list) {
        if (list == null || list.isEmpty()) {
            return new int[0];
        }
        int[] result = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }
        return result;
    }
}
