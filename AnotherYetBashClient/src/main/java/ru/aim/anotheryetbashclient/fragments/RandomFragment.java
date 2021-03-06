package ru.aim.anotheryetbashclient.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;

import ru.aim.anotheryetbashclient.ActionsAndIntents;
import ru.aim.anotheryetbashclient.RecycleQuotesAdapter;
import ru.aim.anotheryetbashclient.loaders.RandomLoader;
import ru.aim.anotheryetbashclient.loaders.SimpleLoaderCallbacks;
import ru.aim.anotheryetbashclient.loaders.SimpleResult;

public class RandomFragment extends AbstractFragment implements SimpleLoaderCallbacks<Cursor> {

    @Override
    public void onManualUpdate() {
        setRefreshing(true);
        if (getLoaderManager().getLoader(RandomLoader.ID) == null) {
            getLoaderManager().initLoader(RandomLoader.ID, Bundle.EMPTY, this);
        } else {
            getLoaderManager().getLoader(RandomLoader.ID).forceLoad();
        }
    }

    @Override
    public int getType() {
        return ActionsAndIntents.TYPE_RANDOM;
    }

    @Override
    protected void initLoader() {
        getLoaderManager().initLoader(RandomLoader.ID, Bundle.EMPTY, this);
    }

    @Override
    public Loader<SimpleResult<Cursor>> onCreateLoader(int id, Bundle args) {
        setRefreshing(true);
        return new RandomLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<SimpleResult<Cursor>> loader, SimpleResult<Cursor> data) {
        setRefreshing(false);
        if (data.containsError()) {
            showWarning(getActivity(), data.getError().getMessage());
        } else {
            RecycleQuotesAdapter listAdapter = new RecycleQuotesAdapter(getActivity(), data.getResult());
            setAdapter(listAdapter);
            setMenuItemsVisibility(true);
        }
    }

    @Override
    public void onLoaderReset(Loader<SimpleResult<Cursor>> loader) {
        safeSwap();
    }
}
