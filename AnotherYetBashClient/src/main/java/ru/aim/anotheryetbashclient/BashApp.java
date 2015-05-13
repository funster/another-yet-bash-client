package ru.aim.anotheryetbashclient;

import android.app.Application;
import android.net.http.AndroidHttpClient;

import org.apache.http.client.params.HttpClientParams;

public class BashApp extends Application {

    private static BashApp instance;

    AndroidHttpClient httpClient;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        httpClient = AndroidHttpClient.newInstance(System.getProperty("http.agent", "Android"), this);
        HttpClientParams.setRedirecting(httpClient.getParams(), true);
    }

    public AndroidHttpClient getHttpClient() {
        return httpClient;
    }

    public static BashApp getInstance() {
        return instance;
    }
}
