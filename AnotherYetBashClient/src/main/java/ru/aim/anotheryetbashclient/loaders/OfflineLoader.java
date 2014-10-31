package ru.aim.anotheryetbashclient.loaders;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

public class OfflineLoader extends AbstractLoader<Cursor> {

    int page = 0;

    public OfflineLoader(Context context, Bundle bundle) {
        super(context);
        page = bundle.getInt("page", 0);
    }

    @Override
    public Cursor doInBackground() throws Exception {
        if (page == -1) {
            return getDbHelper().selectFromOffline();
        } else {
            Cursor result = getDbHelper().selectFromOffline(page);
            if (result.getCount() == 0) {
                result.close();
                return getDbHelper().selectFromOffline();
            }
            return result;
        }
    }
}
