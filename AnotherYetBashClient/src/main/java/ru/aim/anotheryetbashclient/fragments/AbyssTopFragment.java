package ru.aim.anotheryetbashclient.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;

import ru.aim.anotheryetbashclient.ActionsAndIntents;
import ru.aim.anotheryetbashclient.RecycleQuotesAdapter;
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
        if (getLoaderManager().getLoader(AbyssTopLoader.ID) == null) {
            initLoader();
        } else {
            getLoaderManager().getLoader(AbyssTopLoader.ID).forceLoad();
        }
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
            RecycleQuotesAdapter listAdapter = new AbyssTopAdapter(getActivity(), data.getResult());
            setAdapter(listAdapter);
            setMenuItemsVisibility(true);
        }
    }

    @Override
    public void onLoaderReset(Loader<SimpleResult<Cursor>> loader) {
        safeSwap();
    }

    static class AbyssTopAdapter extends RecycleQuotesAdapter {

        public AbyssTopAdapter(Context context, Cursor cursor) {
            super(context, cursor);
        }

        @Override
        public void onBindViewHolder(QuotesViewHolder viewHolder, Cursor cursor) {
            super.onBindViewHolder(viewHolder, cursor);
            viewHolder.plus.setVisibility(View.GONE);
            viewHolder.minus.setVisibility(View.GONE);
            viewHolder.bayan.setVisibility(View.GONE);
            String position = "#" + (cursor.getPosition() + 1);
            viewHolder.id.setText(position);
        }

    }
}
