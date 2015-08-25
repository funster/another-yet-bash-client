package ru.aim.anotheryetbashclient.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public interface ISqlDao extends ISqlDbAware {

    String ID = "id";

    void onCreate(SQLiteDatabase db);

    void insert(ContentValues values);

    Cursor findById(String id);

    Cursor findAll();

    String getTableName();

}
