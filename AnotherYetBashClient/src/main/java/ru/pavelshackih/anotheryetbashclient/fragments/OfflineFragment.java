package ru.pavelshackih.anotheryetbashclient.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import ru.pavelshackih.anotheryetbashclient.ActionsAndIntents;
import ru.pavelshackih.anotheryetbashclient.QuotesAdapter;
import ru.pavelshackih.anotheryetbashclient.R;
import ru.pavelshackih.anotheryetbashclient.SettingsHelper;
import ru.pavelshackih.anotheryetbashclient.loaders.OfflineLoader;
import ru.pavelshackih.anotheryetbashclient.loaders.SimpleResult;

public class OfflineFragment extends AbstractFragment implements LoaderManager.LoaderCallbacks<SimpleResult<Cursor>> {

    DateFormat dateFormat = new SimpleDateFormat("HH:mm dd.MM.yyyy");

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setEmptyView(view.findViewById(android.R.id.empty));
        TextView date = (TextView) view.findViewById(R.id.update_date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(SettingsHelper.getTimestamp(getActivity()));
        date.setText(getString(R.string.last_update, dateFormat.format(calendar.getTime())));
        TextView emptyTextView = (TextView) view.findViewById(android.R.id.empty);
        emptyTextView.setText(R.string.offline_hint);
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().initLoader(1, Bundle.EMPTY, this);
    }

    @Override
    public int getType() {
        return ActionsAndIntents.TYPE_OFFLINE;
    }

    @Override
    public void onManualUpdate() {
        getLoaderManager().getLoader(1).forceLoad();
    }

    @Override
    public Loader<SimpleResult<Cursor>> onCreateLoader(int i, Bundle bundle) {
        setRefreshing(true);
        return new OfflineLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<SimpleResult<Cursor>> loader, SimpleResult<Cursor> result) {
        setRefreshing(false);
        if (result.containsError()) {
            showWarning(getActivity(), result.getError().getMessage());
        } else {
            ListAdapter listAdapter = new QuotesAdapter(getDbHelper(), getActivity(), result.getResult());
            setListAdapter(listAdapter);
            setMenuItemsVisibility(true);
        }
    }

    @Override
    public void onLoaderReset(Loader<SimpleResult<Cursor>> loader) {
        safeSwap();
    }

    @Override
    protected int getRootLayoutId() {
        return R.layout.fragment_list_fresh;
    }
}