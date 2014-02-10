package ru.aim.anotheryetbashclient.helper;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.http.AndroidHttpClient;
import android.os.Bundle;
import ru.aim.anotheryetbashclient.helper.f.Action;

@SuppressWarnings("unused")
public abstract class BaseAction implements HttpAware, SqlDbAware, DbHelperAware, ContextAware, IntentAware, Action {

    private AndroidHttpClient httpClient;
    private SQLiteDatabase db;
    private DbHelper dbHelper;
    private Context context;
    private Intent intent;

    @Override
    public final void setHttpClient(AndroidHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public final void setSqlDb(SQLiteDatabase db) {
        this.db = db;
    }

    @Override
    public void setDbHelper(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public AndroidHttpClient getHttpClient() {
        return httpClient;
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    public DbHelper getDbHelper() {
        return dbHelper;
    }

    public Context getContext() {
        return context;
    }

    public Intent getIntent() {
        return intent;
    }
}
