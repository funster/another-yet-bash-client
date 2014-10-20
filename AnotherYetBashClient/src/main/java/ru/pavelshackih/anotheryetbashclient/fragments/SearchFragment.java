package ru.pavelshackih.anotheryetbashclient.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.SearchView;

import ru.pavelshackih.anotheryetbashclient.ActionsAndIntents;
import ru.pavelshackih.anotheryetbashclient.QuotesAdapter;
import ru.pavelshackih.anotheryetbashclient.R;
import ru.pavelshackih.anotheryetbashclient.loaders.SearchLoader;
import ru.pavelshackih.anotheryetbashclient.loaders.SimpleResult;

public class SearchFragment extends AbstractFragment implements
        LoaderManager.LoaderCallbacks<SimpleResult<Cursor>>, SearchView.OnQueryTextListener {

    SearchView searchView;

    @Override
    public int getType() {
        return ActionsAndIntents.TYPE_SEARCH;
    }

    @Override
    public void onManualUpdate() {
        sendRequest();
    }

    void sendRequest() {
        if (!TextUtils.isEmpty(searchView.getQuery())) {
            setRefreshing(true);
            getLoaderManager().restartLoader(1, bundle(), this);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search, menu);
        final MenuItem menuItem = menu.findItem(R.id.search);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setOnQueryTextListener(this);
        menuItem.expandActionView();
    }

    Bundle bundle() {
        Bundle bundle = new Bundle();
        bundle.putString("search", searchView.getQuery().toString());
        return bundle;
    }

    @Override
    public Loader<SimpleResult<Cursor>> onCreateLoader(int id, Bundle args) {
        return new SearchLoader(getActivity(), args);
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
        safeSwap();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        sendRequest();
        searchView.clearFocus();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
