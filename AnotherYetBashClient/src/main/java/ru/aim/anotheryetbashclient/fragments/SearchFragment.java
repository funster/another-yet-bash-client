package ru.aim.anotheryetbashclient.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;

import ru.aim.anotheryetbashclient.ActionsAndIntents;
import ru.aim.anotheryetbashclient.QuotesAdapter;
import ru.aim.anotheryetbashclient.R;
import ru.aim.anotheryetbashclient.helper.Utils;
import ru.aim.anotheryetbashclient.loaders.SearchLoader;
import ru.aim.anotheryetbashclient.loaders.SimpleResult;

public class SearchFragment extends AbstractFragment implements TextView.OnEditorActionListener,
        LoaderManager.LoaderCallbacks<SimpleResult<Cursor>> {

    EditText searchEditText;

    @Override
    protected int getRootLayoutId() {
        return R.layout.fragment_search_list;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchEditText = (EditText) view.findViewById(android.R.id.edit);
        searchEditText.setOnEditorActionListener(this);
    }

    @Override
    public int getType() {
        return ActionsAndIntents.TYPE_SEARCH;
    }

    @Override
    public void onManualUpdate() {
        sendRequest();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        sendRequest();
        return true;
    }

    void sendRequest() {
        if (!TextUtils.isEmpty(searchEditText.getText())) {
            setRefreshing(true);
            getLoaderManager().restartLoader(1, bundle(), this);
        }
    }

    Bundle bundle() {
        Bundle bundle = new Bundle();
        bundle.putString("search", searchEditText.getText().toString());
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
            showWarning(getFragmentManager(), data.getError().getMessage());
        } else {
            ListAdapter listAdapter = new QuotesAdapter(getDbHelper(), getActivity(), data.getResult());
            setListAdapter(listAdapter);
            setMenuItemsVisibility(true);
            if (data.getResult().getCount() > 0) {
                Utils.hideSoftKeyboard(searchEditText);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<SimpleResult<Cursor>> loader) {
        safeSwap();
    }
}
