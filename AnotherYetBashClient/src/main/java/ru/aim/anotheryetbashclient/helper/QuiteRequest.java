package ru.aim.anotheryetbashclient.helper;

import android.database.sqlite.SQLiteDatabase;
import android.net.http.AndroidHttpClient;

import java.io.IOException;

public interface QuiteRequest {

    void doRequest(SQLiteDatabase database, AndroidHttpClient httpClient) throws IOException;
}
