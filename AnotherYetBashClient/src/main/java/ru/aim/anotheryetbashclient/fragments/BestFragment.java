package ru.aim.anotheryetbashclient.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.*;
import android.widget.ListAdapter;
import android.widget.TextView;
import ru.aim.anotheryetbashclient.ActionsAndIntents;
import ru.aim.anotheryetbashclient.QuotesAdapter;
import ru.aim.anotheryetbashclient.R;
import ru.aim.anotheryetbashclient.helper.DbHelper;
import ru.aim.anotheryetbashclient.loaders.BestLoader;
import ru.aim.anotheryetbashclient.loaders.RandomLoader;
import ru.aim.anotheryetbashclient.loaders.SimpleLoaderCallbacks;
import ru.aim.anotheryetbashclient.loaders.SimpleResult;

public class BestFragment extends AbstractFragment implements SimpleLoaderCallbacks<Cursor> {

    DateResult dateResult = new DateResult();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            dateResult = (DateResult) savedInstanceState.getSerializable("dateResult");
        }
    }

    @Override
    public int getType() {
        return ActionsAndIntents.TYPE_BEST;
    }

    @Override
    protected void initLoader() {
        getLoaderManager().initLoader(RandomLoader.ID, Bundle.EMPTY, this);
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
    public Loader<SimpleResult<Cursor>> onCreateLoader(int id, Bundle args) {
        setRefreshing(true);
        return new BestLoader(getActivity(), args);
    }

    @Override
    public void onLoadFinished(Loader<SimpleResult<Cursor>> loader, SimpleResult<Cursor> data) {
        setRefreshing(false);
        if (data.containsError()) {
            showWarning(getActivity(), data.getError().getMessage());
        } else {
            ListAdapter listAdapter;
            if (dateResult.isToday()) {
                listAdapter = new BestAdapter(getDbHelper(), getActivity(), data.getResult());
            } else {
                listAdapter = new QuotesAdapter(getDbHelper(), getActivity(), data.getResult());
            }
            setListAdapter(listAdapter);
            setMenuItemsVisibility(true);
        }
    }

    @Override
    public void onLoaderReset(Loader<SimpleResult<Cursor>> loader) {
        safeSwap();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.best, menu);
        MenuItem menuItem = menu.findItem(R.id.action_today);
        if (dateResult.isToday()) {
            menuItem.setTitle(R.string.today);
        } else {
            menuItem.setTitle(String.format("%s, %s",
                    getResources().getStringArray(R.array.months)[dateResult.month - 1],
                    dateResult.year));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_today) {
            DateDialog dateDialog = new CustomDateDialog();
            dateDialog.show(getFragmentManager(), "dateDialog");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onMessageReceived(Object message) {
        super.onMessageReceived(message);
        if (message instanceof DateResult) {
            setRefreshing(true);
            dateResult = (DateResult) message;
            getActivity().invalidateOptionsMenu();
            getLoaderManager().restartLoader(BestLoader.ID, buildArgs(), this);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("dateResult", dateResult);
    }

    public static class CustomDateDialog extends DateDialog {

        @Override
        Class<? extends BaseFragment> sendTo() {
            return BestFragment.class;
        }
    }

    /**
     * Adapter for first page.
     */
    public static class BestAdapter extends QuotesAdapter {

        int monthTitlePosition;

        public BestAdapter(DbHelper dbHelper, Context context, Cursor cursor) {
            super(dbHelper, context, cursor);
            monthTitlePosition = 0;
            initSecondTitle();
        }

        void initSecondTitle() {
            getCursor().moveToFirst();
            while (getCursor().moveToNext()) {
                int flag = getCursor().getInt(getCursor().getColumnIndex(DbHelper.QUOTE_FLAG));
                if (flag == 1) {
                    break;
                }
                monthTitlePosition++;
            }
        }

        @Override
        public int getCount() {
            if (getCursor() == null) return 0;
            return getCursor().getCount() + 2;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (position == 0) {
                TextView textView = (TextView) View.inflate(mContext, R.layout.best_title_item, null);
                textView.setText(R.string.best_of_day);
                return textView;
            } else if (position == monthTitlePosition) {
                TextView textView = (TextView) View.inflate(mContext, R.layout.best_title_item, null);
                textView.setText(R.string.best_of_week);
                return textView;
            } else {
                if (monthTitlePosition == 0) {
                    initSecondTitle();
                }
                if (position < monthTitlePosition) {
                    position -= 1;
                } else {
                    position -= 2;
                }
                return super.getView(position, convertView, parent);
            }
        }

        @Override
        public boolean isEnabled(int position) {
            return position != 0 && position != monthTitlePosition;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            return position == 0 || position == monthTitlePosition ? 1 : 0;
        }
    }
}
