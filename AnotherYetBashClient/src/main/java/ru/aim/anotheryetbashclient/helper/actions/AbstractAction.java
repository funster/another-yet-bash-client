package ru.aim.anotheryetbashclient.helper.actions;

import android.content.Context;
import android.net.http.AndroidHttpClient;
import android.os.Bundle;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import ru.aim.anotheryetbashclient.helper.DbHelper;

public abstract class AbstractAction implements IAction, IContextAware, IDbAware, IHttpClientAware, IBundleAware {

    private Context mContext;
    private DbHelper mDbHelper;
    private HttpClient mHttpClient;
    private Bundle mArguments = Bundle.EMPTY;

    @Override
    public void setContext(Context context) {
        this.mContext = context;
    }

    @Override
    public void setDbHelper(DbHelper dbHelper) {
        this.mDbHelper = dbHelper;
    }

    @Override
    public void setHttpClient(HttpClient httpClient) {
        this.mHttpClient = httpClient;
    }

    public Context getContext() {
        return mContext;
    }

    public DbHelper getDbHelper() {
        return mDbHelper;
    }

    public HttpClient getHttpClient() {
        return mHttpClient;
    }

    @Override
    public Bundle getArguments() {
        return mArguments;
    }

    @Override
    public void setBundle(Bundle bundle) {
        this.mArguments = bundle;
    }

    HttpGet buildGetRequest(String url) {
        HttpGet httpRequest = new HttpGet(url);
        AndroidHttpClient.modifyRequestToAcceptGzipResponse(httpRequest);
        return httpRequest;
    }
}
