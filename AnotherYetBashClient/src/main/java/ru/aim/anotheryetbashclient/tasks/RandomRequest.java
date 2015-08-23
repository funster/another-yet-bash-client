package ru.aim.anotheryetbashclient.tasks;

import android.content.ContentValues;

import ru.aim.anotheryetbashclient.QType;
import ru.aim.anotheryetbashclient.helper.DbHelper;

public class RandomRequest extends QRequest {

    @Override
    void afterParsing() {
    }

    @Override
    String getUrl() {
        return null;
    }


    @Override
    QType getType() {
        return QType.RANDOM;
    }
}
