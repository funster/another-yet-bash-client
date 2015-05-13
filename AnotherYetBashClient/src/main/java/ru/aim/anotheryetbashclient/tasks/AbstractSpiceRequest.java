package ru.aim.anotheryetbashclient.tasks;

import com.octo.android.robospice.request.okhttp.OkHttpSpiceRequest;
import com.squareup.okhttp.Request;

public class AbstractSpiceRequest extends OkHttpSpiceRequest<String> {

    public AbstractSpiceRequest() {
        super(String.class);
    }

    @Override
    public String loadDataFromNetwork() throws Exception {

        Request request = new Request.Builder().
        getOkHttpClient().newCall()
        return null;
    }
}
