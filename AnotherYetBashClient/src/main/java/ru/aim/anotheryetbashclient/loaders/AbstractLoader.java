package ru.aim.anotheryetbashclient.loaders;

import android.content.Context;
import ru.aim.anotheryetbashclient.helper.DbHelper;
import ru.aim.anotheryetbashclient.helper.L;

/**
 *
 */
abstract class AbstractLoader<T> extends SimpleLoader<SimpleResult<T>> {

    static final String TAG = "AbstractSbtLoader";

    DbHelper dbHelper;

    public AbstractLoader(Context context) {
        super(context);
    }

    public DbHelper getDbHelper() {
        return dbHelper;
    }

    @Override
    public final SimpleResult<T> loadInBackground() {
        Exception exc = null;
        T result = null;
        try {
            result = doInBackground();
        } catch (Exception e1) {
            L.e(TAG, "Error in loader", e1);
            exc = e1;
        }
        return new SimpleResult<T>(exc, result);
    }

    @Override
    protected void onRelease() {
        super.onRelease();
    }

    @Override
    protected void onStartLoading() {
        dbHelper = new DbHelper(getContext());
        super.onStartLoading();
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
        dbHelper.close();
    }

    public abstract T doInBackground() throws Exception;
}
