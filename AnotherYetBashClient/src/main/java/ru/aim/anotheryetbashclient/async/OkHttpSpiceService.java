package ru.aim.anotheryetbashclient.async;

import com.octo.android.robospice.SpiceService;
import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.request.listener.RequestListener;
import com.squareup.okhttp.OkHttpClient;

import java.util.Set;

/**
 * Pavel : 14.05.2015.
 */
public abstract class OkHttpSpiceService extends SpiceService {

    private OkHttpClient mOkHttpClient;

    @Override
    public void onCreate() {
        super.onCreate();
        mOkHttpClient = createHttpClient();
    }

    public OkHttpClient createHttpClient() {
        return new OkHttpClient();
    }

    @Override
    public void addRequest(CachedSpiceRequest<?> request, Set<RequestListener<?>> listRequestListener) {
        if (request.getSpiceRequest() instanceof OkHttpSpiceRequest) {
            OkHttpSpiceRequest okHttpSpiceRequest = (OkHttpSpiceRequest) request.getSpiceRequest();
            okHttpSpiceRequest.setOkHttpClient(mOkHttpClient);
        }
        super.addRequest(request, listRequestListener);
    }

    public OkHttpClient getOkHttpClient() {
        return new OkHttpClient();
    }
}
