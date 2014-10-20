package ru.aim.anotheryetbashclient.loaders;

import android.content.Context;
import android.database.Cursor;

public class OfflineLoader extends AbstractLoader<Cursor> {

    public OfflineLoader(Context context) {
        super(context);
    }

    @Override
    public Cursor doInBackground() throws Exception {
        return getDbHelper().selectFromFresh();
    }
}
