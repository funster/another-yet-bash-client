package ru.aim.anotheryetbashclient.tasks;

import android.net.http.AndroidHttpClient;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

final class Package {

    static HttpUriRequest getHttpRequest(String url) {
        HttpGet httpGet = new HttpGet(url);
        AndroidHttpClient.modifyRequestToAcceptGzipResponse(httpGet);
        return httpGet;
    }

    static InputStream getInputStream(HttpResponse httpResponse) throws IOException {
        InputStream inputStream = httpResponse.getEntity().getContent();
        Header contentEncoding = httpResponse.getFirstHeader("Content-Encoding");
        if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
            inputStream = new GZIPInputStream(inputStream);
        }
        return inputStream;
    }
}
