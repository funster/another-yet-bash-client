package ru.aim.anotheryetbashclient.loaders;

import android.net.http.AndroidHttpClient;
import org.apache.http.client.params.HttpClientParams;

/**
 *
 */
public class Utils {

    static volatile AndroidHttpClient httpClient;

    public static AndroidHttpClient getHttpClient() {
        if (httpClient == null) {
            synchronized (Utils.class) {
                if (httpClient == null) {
                    httpClient = AndroidHttpClient.newInstance(System.getProperty("http.agent", "Android"));
                    HttpClientParams.setRedirecting(httpClient.getParams(), true);
                }
            }
        }
        return httpClient;
    }
}
