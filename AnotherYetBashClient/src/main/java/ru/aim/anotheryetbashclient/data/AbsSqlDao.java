package ru.aim.anotheryetbashclient.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public abstract class AbsSqlDao implements ISqlDao {

    @Override
    public void insert(ContentValues values) {
        getDb().insert(getTableName(), null, values);
    }

    @Override
    public Cursor findAll() {
        return getDb().rawQuery("select * from " + getTableName(), null);
    }

    @Override
    public Cursor findById(String id) {
        return getDb().rawQuery("select * from " + getTableName() +
                " where " + ISqlDao.ID + " = ?", new String[]{id});
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void setDb(SQLiteDatabase db) {
    }

    @Override
    public SQLiteDatabase getDb() {
        return null;
    }
}
