package ru.aim.anotheryetbashclient.fragments;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import ru.aim.anotheryetbashclient.ActionsAndIntents;
import ru.aim.anotheryetbashclient.R;
import ru.aim.anotheryetbashclient.RecycleQuotesAdapter;
import ru.aim.anotheryetbashclient.loaders.FreshLoader;
import ru.aim.anotheryetbashclient.loaders.RatingLoader;
import ru.aim.anotheryetbashclient.loaders.SimpleLoaderCallbacks;
import ru.aim.anotheryetbashclient.loaders.SimpleResult;

import static ru.aim.anotheryetbashclient.ActionsAndIntents.CURRENT_PAGE;
import static ru.aim.anotheryetbashclient.loaders.RatingLoader.RatingResult;

public class RatingFragment extends AbstractFragment implements SimpleLoaderCallbacks<Cursor> {

    @Override
    public int getType() {
        return ActionsAndIntents.TYPE_BY_RATING;
    }

    @Override
    protected void initLoader() {
        getLoaderManager().initLoader(FreshLoader.ID, Bundle.EMPTY, this);
    }

    int currentPage = 1;
    int maxPage;

    @Override
    public void onManualUpdate() {
        restartLoader(Bundle.EMPTY);
    }

    Bundle buildArguments() {
        Bundle bundle = new Bundle();
        bundle.putInt(CURRENT_PAGE, currentPage);
        return bundle;
    }

    void restartLoader(Bundle arg) {
        setRefreshing(true);
        getLoaderManager().restartLoader(FreshLoader.ID, arg, this);
    }

    @Override
    public Loader<SimpleResult<Cursor>> onCreateLoader(int id, Bundle args) {
        setRefreshing(true);
        return new RatingLoader(getActivity(), args);
    }

    @Override
    public void onLoadFinished(Loader<SimpleResult<Cursor>> loader, final SimpleResult<Cursor> data) {
        setRefreshing(false);
        if (data.containsError()) {
            showWarning(getActivity(), data.getError().getMessage());
        } else {
            RecycleQuotesAdapter listAdapter = new RecycleQuotesAdapter(getActivity(), data.getResult());
            setAdapter(listAdapter);
            RatingResult ratingResult = (RatingResult) data.getTag();
            if (ratingResult != null) {
                currentPage = ratingResult.currentPage;
                maxPage = ratingResult.maxPage;
            }
            setMenuItemsVisibility(true);
        }
    }

    @Override
    public void onLoaderReset(Loader<SimpleResult<Cursor>> loader) {
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.news, menu);
        MenuItem menuItem = menu.findItem(R.id.action_page);
        menuItem.setTitle(Integer.toString(currentPage));
        checkItemsVisibility(menu);
        if (isItemsVisible()) {
            menu.findItem(R.id.action_back).setVisible(currentPage != 1);
            menu.findItem(R.id.action_forward).setVisible(currentPage != maxPage);
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
            RatingPickerDialog dialog = new RatingPickerDialog();
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

    public static class RatingPickerDialog extends NumberPickerDialog {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            sendMessage(getActivity(), RatingFragment.class, picker.getValue());
        }
    }
}
