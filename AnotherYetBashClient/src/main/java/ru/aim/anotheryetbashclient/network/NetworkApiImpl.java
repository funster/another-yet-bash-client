package ru.aim.anotheryetbashclient.network;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.InputStream;

public class NetworkApiImpl implements INetworkApi {

    static final String WINDOWS_1251 = "windows-1251";

    final OkHttpClient client = new OkHttpClient();

    @Override
    public ResponseWrapper performRequest(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        InputStream is = response.body().byteStream();
        String charset = response.header("content-type", WINDOWS_1251);
        return new ResponseWrapper(is, charset);
    }
}
