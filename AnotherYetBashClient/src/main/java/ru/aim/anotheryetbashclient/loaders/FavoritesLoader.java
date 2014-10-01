package ru.aim.anotheryetbashclient.loaders;

import android.content.Context;
import android.database.Cursor;

public class FavoritesLoader extends AbstractLoader<Cursor> {

    public FavoritesLoader(Context context) {
        super(context);
    }

    @Override
    public Cursor doInBackground() throws Exception {
        return getDbHelper().selectFromFavorites();
    }
}
