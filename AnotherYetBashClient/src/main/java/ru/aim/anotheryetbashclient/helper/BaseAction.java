package ru.aim.anotheryetbashclient.helper;

import android.database.sqlite.SQLiteDatabase;
import android.net.http.AndroidHttpClient;
import ru.aim.anotheryetbashclient.helper.f.Action;

public abstract class BaseAction implements HttpAware, SqlDbAware, DbHelperAware, Action {

    protected AndroidHttpClient httpClient;
    protected SQLiteDatabase db;
    protected DbHelper dbHelper;

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
}
