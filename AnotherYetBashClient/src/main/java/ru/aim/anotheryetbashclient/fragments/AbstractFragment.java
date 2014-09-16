package ru.aim.anotheryetbashclient.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import ru.aim.anotheryetbashclient.MainActivity;
import ru.aim.anotheryetbashclient.R;
import ru.aim.anotheryetbashclient.helper.DbHelper;

import static ru.aim.anotheryetbashclient.helper.Utils.setItemsVisibility;

/**
 *
 */
@SuppressWarnings("unused")
public abstract class AbstractFragment extends RefreshFragment implements AdapterView.OnItemLongClickListener {

    private DbHelper mDbHelper;
    private int mCurrentType;
    private boolean isRefreshing;
    private MainActivity mainActivity;

    protected Cursor cursor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list, null);
        setHasOptionsMenu(true);
        mDbHelper = new DbHelper(getActivity());
        ListView listView = (ListView) root.findViewById(android.R.id.list);
//        listView.setEmptyView(root.findViewById(android.R.id.empty));
        listView.setOnItemLongClickListener(this);
        return root;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }

    protected DbHelper getDbHelper() {
        return mDbHelper;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDbHelper.close();
    }

    public abstract int getType();

    protected void checkItemsVisibility(Menu menu) {
        setItemsVisibility(menu, !isRefreshing());
        if (!isRefreshing()) {
            setItemsVisibility(menu, isItemsVisible());
        }
    }
}
