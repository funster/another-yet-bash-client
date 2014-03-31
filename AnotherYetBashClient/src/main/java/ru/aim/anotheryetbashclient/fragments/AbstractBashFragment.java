package ru.aim.anotheryetbashclient.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import ru.aim.anotheryetbashclient.*;
import ru.aim.anotheryetbashclient.helper.DbHelper;
import ru.aim.anotheryetbashclient.helper.ObjectSerializer;

import java.util.ArrayList;

import static ru.aim.anotheryetbashclient.ActionsAndIntents.CURRENT_QUOTES;
import static ru.aim.anotheryetbashclient.helper.Utils.isNetworkAvailable;
import static ru.aim.anotheryetbashclient.helper.Utils.setItemsVisibility;

/**
 *
 */
@SuppressWarnings("unused")
public abstract class AbstractBashFragment extends ListFragment
        implements AdapterView.OnItemLongClickListener, SwipeRefreshLayout.OnRefreshListener {

    private boolean itemsVisible = true;
    private DbHelper mDbHelper;
    private int mCurrentType;
    private boolean isRefreshing;
    private MainActivity mainActivity;
    private SwipeRefreshLayout swipeRefreshLayout;

    protected Cursor cursor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list, null);
        setHasOptionsMenu(true);
        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorScheme(
                android.R.color.holo_green_light,
                android.R.color.holo_blue_dark,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mDbHelper = new DbHelper(getActivity());
        ListView listView = (ListView) root.findViewById(android.R.id.list);
        listView.setOnItemLongClickListener(this);
        int savedType = SettingsHelper.loadType(getActivity());
        if (savedType == getType() && isSavedCursorExists()) {
            loadData();
        } else {
            afterViewCreated();
        }
        return root;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }

    protected void afterViewCreated() {
    }

    protected void loadData() {
        String[] ids = getSavedCursor();
        cursor = getDbHelper().getQuotes(ids);
        setListAdapter(new QuotesAdapter(getDbHelper(), getActivity(), cursor));
    }

    protected final DbHelper getDbHelper() {
        return mDbHelper;
    }

    protected final int getCurrentType() {
        return mCurrentType;
    }

    final void setRefreshing(boolean value) {
        swipeRefreshLayout.setRefreshing(value);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDbHelper.close();
    }

    public final void doReceive(Intent intent) {
        isRefreshing = false;
        setRefreshing(false);
        onReceive(intent);
    }

    public abstract void onReceive(Intent intent);

    public abstract void onManualUpdate();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            onManualUpdate();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void doStartService(Intent intent) {
        setRefreshing(true);
        isRefreshing = true;
        getActivity().invalidateOptionsMenu();
        getActivity().startService(intent);
    }

    public boolean isRefreshing() {
        return isRefreshing;
    }

    public abstract int getType();

    protected void checkItemsVisibility(Menu menu) {
        setItemsVisibility(menu, !isRefreshing());
        if (!isRefreshing()) {
            setItemsVisibility(menu, isItemsVisible());
        }
    }

    MainActivity getInternalActivity() {
        if (mainActivity == null && getActivity() instanceof MainActivity) {
            mainActivity = (MainActivity) getActivity();
        }
        return mainActivity;
    }

    public void setMenuItemsVisibility(boolean visible) {
        itemsVisible = visible;
        getActivity().invalidateOptionsMenu();
    }

    protected void loadCursor(Intent intent) {
        ArrayList<String> list = intent.getStringArrayListExtra(ActionsAndIntents.IDS);
        assert list != null;
        if (!list.isEmpty()) {
            String[] arr = list.toArray(new String[list.size()]);
            if (cursor != null) {
                cursor.close();
            }
            cursor = getDbHelper().getQuotes(arr);
        }
    }

    public boolean isItemsVisible() {
        return itemsVisible && !isRefreshing && isOnline();
    }

    public void saveData() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (cursor == null) {
            preferences.edit().remove(CURRENT_QUOTES).commit();
        } else {
            ArrayList<String> list = new ArrayList<>();
            while (cursor.moveToNext()) {
                list.add(cursor.getString(cursor.getColumnIndex(DbHelper.QUOTE_PUBLIC_ID)));
            }
            String bin = ObjectSerializer.serialize(list);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(CURRENT_QUOTES, bin);
            editor.commit();
        }
    }

    boolean isSavedCursorExists() {
        return PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(CURRENT_QUOTES, null) != null;
    }

    @SuppressWarnings("unchecked")
    String[] getSavedCursor() {
        String bin = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(CURRENT_QUOTES, null);
        ArrayList<String> list = (ArrayList<String>) ObjectSerializer.deserialize(bin);
        return list.toArray(new String[list.size()]);
    }

    @Override
    public void onRefresh() {
        onManualUpdate();
    }

    protected boolean isOffline() {
        return !isNetworkAvailable(getActivity());
    }

    protected boolean isOnline() {
        return isNetworkAvailable(getActivity());
    }
}
