package ru.aim.anotheryetbashclient.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.Loader;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;

import java.util.Calendar;

import ru.aim.anotheryetbashclient.ActionsAndIntents;
import ru.aim.anotheryetbashclient.R;
import ru.aim.anotheryetbashclient.RecycleQuotesAdapter;
import ru.aim.anotheryetbashclient.ShareDialog;
import ru.aim.anotheryetbashclient.loaders.AbyssBestLoader;
import ru.aim.anotheryetbashclient.loaders.BestLoader;
import ru.aim.anotheryetbashclient.loaders.SimpleLoaderCallbacks;
import ru.aim.anotheryetbashclient.loaders.SimpleResult;

/**
 *
 */
public class AbyssBestFragment extends AbstractFragment implements SimpleLoaderCallbacks<Cursor> {

    DateResult dateResult = new DateResult();

    @Override
    public int getType() {
        return ActionsAndIntents.TYPE_BEST_ABYSS;
    }

    @Override
    public void onManualUpdate() {
        setRefreshing(true);
        getLoaderManager().restartLoader(BestLoader.ID, buildArgs(), this);
    }

    Bundle buildArgs() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("dateResult", dateResult);
        return bundle;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.best, menu);
        MenuItem menuItem = menu.findItem(R.id.action_today);
        if (dateResult.isToday()) {
            menuItem.setTitle(R.string.today);
        } else {
            menuItem.setTitle(String.format("%s %s, %s",
                    dateResult.day,
                    getResources().getStringArray(R.array.months)[dateResult.month],
                    dateResult.year));
        }
    }

    @Override
    protected void initLoader() {
        getLoaderManager().initLoader(1, buildArgs(), this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_today) {
            DatePickerFragment fragment = new DatePickerFragment();
            fragment.setArguments(buildArgs());
            fragment.show(getFragmentManager(), "date-picker-dialog");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onMessageReceived(Object message) {
        super.onMessageReceived(message);
        dateResult = (DateResult) message;
        getActivity().invalidateOptionsMenu();
        getLoaderManager().restartLoader(1, buildArgs(), this);
    }

    @Override
    public Loader<SimpleResult<Cursor>> onCreateLoader(int i, Bundle bundle) {
        setRefreshing(true);
        return new AbyssBestLoader(getActivity(), bundle);
    }

    @Override
    public void onLoadFinished(Loader<SimpleResult<Cursor>> loader, SimpleResult<Cursor> data) {
        setRefreshing(false);
        if (data.containsError()) {
            showWarning(getActivity(), data.getError().getMessage());
        } else {
            RecycleQuotesAdapter adapter;
            adapter = new AbyssBestAdapter(getActivity(), data.getResult());
            setAdapter(adapter);
            setMenuItemsVisibility(true);
        }
    }

    @Override
    public void onLoaderReset(Loader<SimpleResult<Cursor>> loader) {
        safeSwap();
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener,
            DialogInterface.OnClickListener {

        DateResult dateResult;
        Calendar today = Calendar.getInstance();
        DatePicker datePicker;

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            dateResult = (DateResult) getArguments().getSerializable("dateResult");
            DatePickerDialog dialog;
            if (!dateResult.isToday()) {
                dialog = new DatePickerDialog(getActivity(), this, dateResult.year, dateResult.month, dateResult.day);
            } else {
                dialog = new DatePickerDialog(getActivity(), this,
                        today.get(Calendar.YEAR),
                        today.get(Calendar.MONTH) + 1,
                        today.get(Calendar.DAY_OF_MONTH));
            }
            dialog.getDatePicker().setMaxDate(today.getTimeInMillis());
            today.add(Calendar.YEAR, -1);
            dialog.getDatePicker().setMinDate(today.getTimeInMillis());
            dialog.getDatePicker().setCalendarViewShown(false);
            dialog.setCancelable(true);
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.ok), this);
            dialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.today), this);
            datePicker = dialog.getDatePicker();
            return dialog;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            DateResult dateResult = new DateResult();
            if (which == DialogInterface.BUTTON_POSITIVE) {
                dateResult.year = datePicker.getYear();
                dateResult.month = datePicker.getMonth();
                dateResult.day = datePicker.getDayOfMonth();
                sendMessage(getActivity(), AbyssBestFragment.class, dateResult);
            } else if (which == DialogInterface.BUTTON_NEUTRAL) {
                sendMessage(getActivity(), AbyssBestFragment.class, dateResult);
            }
        }
    }

    static class AbyssBestAdapter extends RecycleQuotesAdapter {


        public AbyssBestAdapter(Context context, Cursor cursor) {
            super(context, cursor);
        }

        @Override
        public void onBindViewHolder(QuotesViewHolder viewHolder, Cursor cursor) {
            super.onBindViewHolder(viewHolder, cursor);
//            viewHolder.plus.setVisibility(View.GONE);
//            viewHolder.minus.setVisibility(View.GONE);
//            viewHolder.bayan.setVisibility(View.GONE);
        }

        @Override
        protected void share(Context context, QuotesViewHolder viewHolder) {
            Bitmap bitmap = buildQuoteBitmap(viewHolder);
            ShareDialog shareDialog = ShareDialog.newInstance(bitmap, null,
                    viewHolder.text.getText().toString());
            if (context instanceof FragmentActivity) {
                FragmentActivity fragmentActivity = (FragmentActivity) context;
                shareDialog.show(fragmentActivity.getSupportFragmentManager(), "share-dialog");
            }
        }
    }
}
