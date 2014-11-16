package ru.aim.anotheryetbashclient;

import android.app.Application;
import android.net.http.AndroidHttpClient;

import org.apache.http.client.params.HttpClientParams;

public class BashApplication extends Application {

    AndroidHttpClient httpClient;

    @Override
    public void onCreate() {
        super.onCreate();
        httpClient = AndroidHttpClient.newInstance(System.getProperty("http.agent", "Android"), this);
        HttpClientParams.setRedirecting(httpClient.getParams(), true);
    }

    public AndroidHttpClient getHttpClient() {
        return httpClient;
    }
}
