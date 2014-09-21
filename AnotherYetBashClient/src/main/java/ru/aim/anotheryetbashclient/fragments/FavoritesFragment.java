package ru.aim.anotheryetbashclient.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListAdapter;

import ru.aim.anotheryetbashclient.ActionsAndIntents;
import ru.aim.anotheryetbashclient.QuotesAdapter;
import ru.aim.anotheryetbashclient.loaders.FavoritesLoader;
import ru.aim.anotheryetbashclient.loaders.SimpleResult;

public class FavoritesFragment extends AbstractFragment
        implements LoaderManager.LoaderCallbacks<SimpleResult<Cursor>> {

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setEmptyView(view.findViewById(android.R.id.empty));
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().initLoader(1, Bundle.EMPTY, this);
    }

    @Override
    public int getType() {
        return ActionsAndIntents.TYPE_FAVORITES;
    }

    @Override
    public void onManualUpdate() {
        getLoaderManager().getLoader(1).forceLoad();
    }

    @Override
    public Loader<SimpleResult<Cursor>> onCreateLoader(int i, Bundle bundle) {
        setRefreshing(true);
        return new FavoritesLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<SimpleResult<Cursor>> loader, SimpleResult<Cursor> result) {
        setRefreshing(false);
        if (result.containsError()) {
            SimpleDialog simpleDialog = SimpleDialog.newInstance(result.getError().getMessage());
            simpleDialog.show(getFragmentManager(), "dialog");
        } else {
            ListAdapter listAdapter = new QuotesAdapter(getDbHelper(), getActivity(), result.getResult());
            setListAdapter(listAdapter);
            setMenuItemsVisibility(true);
        }
    }

    @Override
    public void onLoaderReset(Loader<SimpleResult<Cursor>> loader) {
    }
}