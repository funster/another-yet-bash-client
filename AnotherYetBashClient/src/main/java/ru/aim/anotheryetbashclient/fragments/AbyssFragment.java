package ru.aim.anotheryetbashclient.fragments;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.Loader;
import android.widget.ListAdapter;
import ru.aim.anotheryetbashclient.ActionsAndIntents;
import ru.aim.anotheryetbashclient.QuotesAdapter;
import ru.aim.anotheryetbashclient.ShareDialog;
import ru.aim.anotheryetbashclient.helper.DbHelper;
import ru.aim.anotheryetbashclient.loaders.AbyssLoader;
import ru.aim.anotheryetbashclient.loaders.SimpleLoaderCallbacks;
import ru.aim.anotheryetbashclient.loaders.SimpleResult;

/**
 *
 */
public class AbyssFragment extends AbstractFragment implements SimpleLoaderCallbacks<Cursor> {

    @Override
    public void onManualUpdate() {
        setRefreshing(true);
        getLoaderManager().getLoader(AbyssLoader.ID).forceLoad();
    }

    @Override
    public int getType() {
        return ActionsAndIntents.TYPE_ABYSS;
    }

    @Override
    protected void initLoader() {
        getLoaderManager().initLoader(AbyssLoader.ID, Bundle.EMPTY, this);
    }

    @Override
    public Loader<SimpleResult<Cursor>> onCreateLoader(int id, Bundle args) {
        setRefreshing(true);
        return new AbyssLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<SimpleResult<Cursor>> loader, SimpleResult<Cursor> data) {
        setRefreshing(false);
        if (data.containsError()) {
            showWarning(getActivity(), data.getError().getMessage());
        } else {
            ListAdapter listAdapter = new AbyssAdapter(getDbHelper(), getActivity(), data.getResult());
            setListAdapter(listAdapter);
            setMenuItemsVisibility(true);
        }
    }

    @Override
    public void onLoaderReset(Loader<SimpleResult<Cursor>> loader) {
        safeSwap();
    }

    static class AbyssAdapter extends QuotesAdapter {

        public AbyssAdapter(DbHelper dbHelper, Context context, Cursor c) {
            super(dbHelper, context, c);
        }

        @Override
        protected void share(Context context, ViewHolder viewHolder) {
            viewHolder.quoteContainer.setDrawingCacheEnabled(true);
            Bitmap bitmap = viewHolder.quoteContainer.getDrawingCache();
            ShareDialog shareDialog = ShareDialog.newInstance(bitmap, null,
                    viewHolder.text.getText().toString());
            if (context instanceof FragmentActivity) {
                FragmentActivity fragmentActivity = (FragmentActivity) context;
                shareDialog.show(fragmentActivity.getSupportFragmentManager(), "share-dialog");
            }
        }
    }
}
