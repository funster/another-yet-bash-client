package ru.aim.anotheryetbashclient.tasks;

import android.content.ContentValues;

import ru.aim.anotheryetbashclient.QType;
import ru.aim.anotheryetbashclient.helper.DbHelper;

public abstract class QRequest extends AbstractOkHttpRequest {

    DbHelper dbHelper = DbHelper.getInstance();

    @Override
    protected void saveQuote(ContentValues values) {
        super.saveQuote(values);
        dbHelper.save(getType(), values);
    }

    abstract QType getType();
}
