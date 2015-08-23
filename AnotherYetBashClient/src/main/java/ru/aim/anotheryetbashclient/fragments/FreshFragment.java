package ru.aim.anotheryetbashclient.fragments;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import ru.aim.anotheryetbashclient.ActionsAndIntents;
import ru.aim.anotheryetbashclient.R;
import ru.aim.anotheryetbashclient.RecycleQuotesAdapter;
import ru.aim.anotheryetbashclient.loaders.FreshLoader;
import ru.aim.anotheryetbashclient.loaders.FreshResult;
import ru.aim.anotheryetbashclient.loaders.SimpleLoaderCallbacks;
import ru.aim.anotheryetbashclient.loaders.SimpleResult;

import static ru.aim.anotheryetbashclient.ActionsAndIntents.CURRENT_PAGE;
import static ru.aim.anotheryetbashclient.ActionsAndIntents.MAX_PAGE;

public class FreshFragment extends AbstractFragment implements SimpleLoaderCallbacks<FreshResult> {

    int currentPage;
    int maxPage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onManualUpdate() {
        restartLoader(Bundle.EMPTY);
    }

    Bundle buildArguments() {
        Bundle bundle = new Bundle();
        bundle.putInt(CURRENT_PAGE, currentPage);
        bundle.putInt(MAX_PAGE, maxPage);
        return bundle;
    }

    void restartLoader(Bundle arg) {
        setRefreshing(true);
        getLoaderManager().restartLoader(FreshLoader.ID, arg, this);
    }

    @Override
    public int getType() {
        return ActionsAndIntents.TYPE_NEW;
    }

    @Override
    protected void initLoader() {
        getLoaderManager().initLoader(FreshLoader.ID, Bundle.EMPTY, this);
    }

    @Override
    public Loader<SimpleResult<FreshResult>> onCreateLoader(int id, Bundle args) {
        setRefreshing(true);
        return new FreshLoader(getActivity(), args);
    }

    @Override
    public void onLoadFinished(Loader<SimpleResult<FreshResult>> loader, final SimpleResult<FreshResult> data) {
        setRefreshing(false);
        if (data.containsError()) {
            showWarning(getActivity(), data.getError().getMessage());
        } else {
            FreshResult freshResult = data.getResult();
            RecycleQuotesAdapter listAdapter = new RecycleQuotesAdapter(getActivity(), freshResult.cursor);
            setAdapter(listAdapter);
            currentPage = freshResult.currentPage;
            if (freshResult.maxPage > maxPage) {
                maxPage = data.getResult().maxPage;
            }
            setMenuItemsVisibility(true);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
                getActivity().invalidateOptionsMenu();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<SimpleResult<FreshResult>> loader) {
        safeSwap();
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.news, menu);
        MenuItem menuItem = menu.findItem(R.id.action_page);
        if (currentPage != 0) {
            menuItem.setTitle(Integer.toString(currentPage));
        }
        checkItemsVisibility(menu);
        if (maxPage != 0 && isItemsVisible()) {
            menu.findItem(R.id.action_back).setVisible(currentPage != maxPage);
            menu.findItem(R.id.action_forward).setVisible(currentPage != 0);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_forward) {
            currentPage -= 1;
            restartLoader(buildArguments());
            return true;
        }
        if (item.getItemId() == R.id.action_back) {
            currentPage += 1;
            restartLoader(buildArguments());
            return true;
        }
        if (item.getItemId() == R.id.action_page) {
            FreshPickerDialog dialog = new FreshPickerDialog();
            dialog.setArguments(NumberPickerDialog.buildArgs(currentPage, maxPage));
            dialog.show(getFragmentManager());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onMessageReceived(Object message) {
        super.onMessageReceived(message);
        currentPage = (Integer) message;
        restartLoader(buildArguments());
    }

    public static class FreshPickerDialog extends NumberPickerDialog {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            sendMessage(getActivity(), FreshFragment.class, picker.getValue());
        }
    }
}
