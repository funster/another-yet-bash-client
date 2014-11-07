package ru.aim.anotheryetbashclient.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import ru.aim.anotheryetbashclient.ActionsAndIntents;
import ru.aim.anotheryetbashclient.QuotesAdapter;
import ru.aim.anotheryetbashclient.helper.DbHelper;
import ru.aim.anotheryetbashclient.loaders.AbyssTopLoader;
import ru.aim.anotheryetbashclient.loaders.SimpleLoaderCallbacks;
import ru.aim.anotheryetbashclient.loaders.SimpleResult;

/**
 *
 */
public class AbyssTopFragment extends AbstractFragment implements SimpleLoaderCallbacks<Cursor> {

    @Override
    public void onManualUpdate() {
        setRefreshing(true);
        getLoaderManager().getLoader(AbyssTopLoader.ID).forceLoad();
    }

    @Override
    public int getType() {
        return ActionsAndIntents.TYPE_TOP_ABYSS;
    }

    @Override
    protected void initLoader() {
        getLoaderManager().initLoader(AbyssTopLoader.ID, Bundle.EMPTY, this);
    }

    @Override
    public Loader<SimpleResult<Cursor>> onCreateLoader(int id, Bundle args) {
        setRefreshing(true);
        return new AbyssTopLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<SimpleResult<Cursor>> loader, SimpleResult<Cursor> data) {
        setRefreshing(false);
        if (data.containsError()) {
            showWarning(getActivity(), data.getError().getMessage());
        } else {
            ListAdapter listAdapter = new AbyssTopAdapter(getDbHelper(), getActivity(), data.getResult());
            setListAdapter(listAdapter);
            setMenuItemsVisibility(true);
        }
    }

    @Override
    public void onLoaderReset(Loader<SimpleResult<Cursor>> loader) {
        safeSwap();
    }

    static class AbyssTopAdapter extends QuotesAdapter {

        public AbyssTopAdapter(DbHelper dbHelper, Context context, Cursor c) {
            super(dbHelper, context, c);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
            View view = super.newView(context, cursor, viewGroup);
            ViewHolder viewHolder = (ViewHolder) view.getTag();
            viewHolder.plus.setVisibility(View.GONE);
            viewHolder.minus.setVisibility(View.GONE);
            viewHolder.bayan.setVisibility(View.GONE);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            super.bindView(view, context, cursor);
            ViewHolder viewHolder = (ViewHolder) view.getTag();
            String position = "#" + (cursor.getPosition() + 1);
            viewHolder.id.setText(position);
        }
    }
}
