package ru.aim.anotheryetbashclient;

import android.app.Application;
import android.net.http.AndroidHttpClient;
import android.os.StrictMode;

import org.apache.http.client.params.HttpClientParams;

public class BashApplication extends Application {

    AndroidHttpClient httpClient;

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().build());
        }
        httpClient = AndroidHttpClient.newInstance(System.getProperty("http.agent", "Android"), this);
        HttpClientParams.setRedirecting(httpClient.getParams(), true);
    }

    public AndroidHttpClient getHttpClient() {
        return httpClient;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        try {
            httpClient.close();
        } catch (Exception ignored) {
        }
    }
}
