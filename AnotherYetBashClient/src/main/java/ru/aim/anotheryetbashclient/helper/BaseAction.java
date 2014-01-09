package ru.aim.anotheryetbashclient.helper;

import android.database.sqlite.SQLiteDatabase;
import android.net.http.AndroidHttpClient;
import ru.aim.anotheryetbashclient.helper.f.Action;

public abstract class BaseAction implements HttpAware, SqlDbAware, Action {

    protected AndroidHttpClient httpClient;
    protected SQLiteDatabase db;

    @Override
    public final void setHttpClient(AndroidHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public final void setSqlDb(SQLiteDatabase db) {
        this.db = db;
    }
}
