package ru.aim.anotheryetbashclient.async;

import com.octo.android.robospice.request.SpiceRequest;
import com.squareup.okhttp.OkHttpClient;

/**
 * Pavel : 14.05.2015.
 */
public abstract class OkHttpSpiceRequest<T> extends SpiceRequest<T> {

    private OkHttpClient okHttpClient;

    public OkHttpSpiceRequest(Class<T> clazz) {
        super(clazz);
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public void setOkHttpClient(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }
}
