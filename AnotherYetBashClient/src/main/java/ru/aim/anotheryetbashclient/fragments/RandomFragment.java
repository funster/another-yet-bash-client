package ru.aim.anotheryetbashclient.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.widget.ListAdapter;

import ru.aim.anotheryetbashclient.ActionsAndIntents;
import ru.aim.anotheryetbashclient.QuotesAdapter;
import ru.aim.anotheryetbashclient.loaders.RandomLoader;
import ru.aim.anotheryetbashclient.loaders.SimpleLoaderCallbacks;
import ru.aim.anotheryetbashclient.loaders.SimpleResult;

public class RandomFragment extends AbstractFragment implements SimpleLoaderCallbacks<Cursor> {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().initLoader(RandomLoader.ID, Bundle.EMPTY, this);
    }

    @Override
    public void onManualUpdate() {
        setRefreshing(true);
        getLoaderManager().getLoader(RandomLoader.ID).forceLoad();
    }

    @Override
    public int getType() {
        return ActionsAndIntents.TYPE_RANDOM;
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
            ListAdapter listAdapter = new QuotesAdapter(getDbHelper(), getActivity(), data.getResult());
            setListAdapter(listAdapter);
            setMenuItemsVisibility(true);
        }
    }

    @Override
    public void onLoaderReset(Loader<SimpleResult<Cursor>> loader) {
    }
}
