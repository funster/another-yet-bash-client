package ru.aim.anotheryetbashclient.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import ru.aim.anotheryetbashclient.ActionsAndIntents;
import ru.aim.anotheryetbashclient.QuoteService;
import ru.aim.anotheryetbashclient.QuotesAdapter;
import ru.aim.anotheryetbashclient.R;
import ru.aim.anotheryetbashclient.SettingsHelper;
import ru.aim.anotheryetbashclient.helper.Utils;
import ru.aim.anotheryetbashclient.helper.actions.OfflineDownloaderAction;
import ru.aim.anotheryetbashclient.loaders.OfflineLoader;
import ru.aim.anotheryetbashclient.loaders.SimpleResult;

public class OfflineFragment extends AbstractFragment implements LoaderManager.LoaderCallbacks<SimpleResult<Cursor>> {

    TextView date;
    int currentPage = 0;
    int maxPage = 0;
    DateFormat dateFormat = new SimpleDateFormat("HH:mm dd.MM.yyyy");

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getActivity());
        manager.registerReceiver(refreshReceiver, new IntentFilter(OfflineDownloaderAction.UPDATE_COMPLETED));

        getListView().setEmptyView(view.findViewById(android.R.id.empty));
        date = (TextView) view.findViewById(R.id.update_date);
        TextView emptyTextView = (TextView) view.findViewById(android.R.id.empty);
        emptyTextView.setText(R.string.offline_hint);
        initPages();
    }

    void initPages() {
        Calendar calendar = Calendar.getInstance();
        long timeInMs = SettingsHelper.getTimestamp(getActivity());
        calendar.setTimeInMillis(timeInMs);
        if (timeInMs == 0) {
            date.setVisibility(View.GONE);
        } else {
            date.setText(getString(R.string.last_update, dateFormat.format(calendar.getTime())));
            date.setVisibility(View.VISIBLE);
        }
        currentPage = 0;
        maxPage = getDbHelper().getOfflinePages();
        if (maxPage != 0) {
            maxPage += 1;
        }
    }

    @Override
    public int getType() {
        return ActionsAndIntents.TYPE_OFFLINE;
    }

    @Override
    protected void initLoader() {
        getLoaderManager().initLoader(1, Bundle.EMPTY, this);
    }

    @Override
    public void onManualUpdate() {
        if (Utils.isNetworkNotAvailable(getActivity())) {
            Toast.makeText(getActivity(), R.string.error_no_connection, Toast.LENGTH_LONG).show();
            return;
        }
        setRefreshing(true);
        getActivity().startService(new Intent(getActivity(), QuoteService.class));
    }

    @Override
    public Loader<SimpleResult<Cursor>> onCreateLoader(int i, Bundle bundle) {
        setRefreshing(true);
        return new OfflineLoader(getActivity(), bundle);
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

    BroadcastReceiver refreshReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            run(new Runnable() {
                @Override
                public void run() {
                    initPages();
                    getActivity().invalidateOptionsMenu();
                    initLoader();
                }
            });
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(refreshReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.news, menu);
        MenuItem menuItem = menu.findItem(R.id.action_page);
        menuItem.setTitle(Integer.toString(currentPage + 1));
        checkItemsVisibility(menu);
        if (maxPage != 0 && isItemsVisible()) {
            menu.findItem(R.id.action_back).setVisible(currentPage != 0);
            menu.findItem(R.id.action_forward).setVisible(currentPage != (maxPage - 1));
        }
        if (maxPage == 0) {
            menuItem.setVisible(false);
            menu.findItem(R.id.action_back).setVisible(false);
            menu.findItem(R.id.action_forward).setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_forward) {
            currentPage += 1;
            restartLoader(buildArguments());
            return true;
        }
        if (item.getItemId() == R.id.action_back) {
            currentPage -= 1;
            restartLoader(buildArguments());
            return true;
        }
        if (item.getItemId() == R.id.action_page) {
            OfflinePickerDialog dialog = new OfflinePickerDialog();
            dialog.setArguments(NumberPickerDialog.buildArgs(currentPage + 1, maxPage));
            dialog.show(getFragmentManager());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    Bundle buildArguments() {
        Bundle bundle = new Bundle();
        bundle.putInt("page", currentPage);
        return bundle;
    }

    void restartLoader(Bundle bundle) {
        setRefreshing(true);
        getLoaderManager().restartLoader(1, bundle, this);
    }

    public static class OfflinePickerDialog extends NumberPickerDialog {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            sendMessage(getActivity(), OfflineFragment.class, picker.getValue() - 1);
        }
    }

    @Override
    protected void onMessageReceived(Object message) {
        super.onMessageReceived(message);
        currentPage = (Integer) message;
        restartLoader(buildArguments());
    }
}