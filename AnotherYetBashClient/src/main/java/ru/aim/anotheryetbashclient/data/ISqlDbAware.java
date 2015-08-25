package ru.aim.anotheryetbashclient.data;

import android.database.sqlite.SQLiteDatabase;

public interface ISqlDbAware {

    void setDb(SQLiteDatabase db);

    SQLiteDatabase getDb();
}
