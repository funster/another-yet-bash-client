package ru.aim.anotheryetbashclient.data;

import android.database.sqlite.SQLiteDatabase;

public interface ISqlDao {

    void onCreate(SQLiteDatabase db);

    void onUpdate(SQLiteDatabase db);
}
