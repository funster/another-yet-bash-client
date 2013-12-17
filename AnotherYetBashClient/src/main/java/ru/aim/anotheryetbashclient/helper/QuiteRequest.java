package ru.aim.anotheryetbashclient.helper;

import android.database.sqlite.SQLiteDatabase;
import android.net.http.AndroidHttpClient;

import java.io.IOException;

public interface QuiteRequest {

    public static final String UTF_8 = "UTF-8";
    public static final String WINDOWS_1215 = "windows-1251";

    void doRequest(SQLiteDatabase database, AndroidHttpClient httpClient) throws IOException;
}
