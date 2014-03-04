package ru.aim.anotheryetbashclient;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import ru.aim.anotheryetbashclient.helper.DbHelper;
import ru.aim.anotheryetbashclient.helper.ObjectSerializer;
import ru.aim.anotheryetbashclient.helper.QuoteService;

import java.util.ArrayList;

import static ru.aim.anotheryetbashclient.ActionsAndIntents.*;
import static ru.aim.anotheryetbashclient.Package.updateHeader;

public class QuotesFragment extends Fragment implements AdapterView.OnItemLongClickListener {

    DbHelper dbHelper;
    ListView listView;
    int currentPage;
    String nextPage;
    int currentType;
    boolean abyss;
    RefreshBroadcastReceiver refreshReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            currentPage = savedInstanceState.getInt(ActionsAndIntents.CURRENT_PAGE);
            nextPage = savedInstanceState.getString(ActionsAndIntents.NEXT_PAGE);
            abyss = true;
        }
        View result = inflater.inflate(R.layout.fragment_list, null);
        assert result != null;
        listView = (ListView) result.findViewById(android.R.id.list);
        listView.setEmptyView(result.findViewById(android.R.id.text1));
        listView.setOnItemLongClickListener(this);
        dbHelper = new DbHelper(getActivity());
        if (isSavedCursorNotExists()) {
            callRefresh(currentType);
        } else {
            if (abyss) {
                listView.setAdapter(new QuotesAdapter(dbHelper, getActivity(), dbHelper.getAbyss()));
            } else {
                listView.setAdapter(new QuotesAdapter(dbHelper, getActivity(), dbHelper.getQuotes(getSavedCursor())));
            }
        }
        refreshReceiver = new RefreshBroadcastReceiver(this);
        IntentFilter intentFilter = new IntentFilter(ActionsAndIntents.REFRESH);
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        localBroadcastManager.registerReceiver(refreshReceiver, intentFilter);

        return result;
    }

    public void callRefresh(int currentType) {
        this.currentType = currentType;
        updateHeader(getActivity(), currentType, currentPage);
        Intent intent = new Intent(getActivity(), QuoteService.class);
        intent.putExtra(TYPE_ID, currentType);
        if (currentPage > 0) {
            intent.putExtra(ActionsAndIntents.CURRENT_PAGE, currentPage);
        }
        if (nextPage != null) {
            intent.putExtra(ActionsAndIntents.NEXT_PAGE, nextPage);
        }
        getActivity().startService(intent);
        getActivity().setProgressBarIndeterminateVisibility(true);
    }

    public void setCurrentType(int currentType) {
        this.currentType = currentType;
    }

    boolean isSavedCursorNotExists() {
        return PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(CURRENT_QUOTES, null) == null;
    }

    @SuppressWarnings("unchecked")
    String[] getSavedCursor() {
        String bin = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(CURRENT_QUOTES, null);
        ArrayList<String> list = (ArrayList<String>) ObjectSerializer.deserialize(bin);
        return list.toArray(new String[list.size()]);
    }

    @Override
    public void onDestroyView() {
        dbHelper.close();
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        localBroadcastManager.unregisterReceiver(refreshReceiver);
        super.onDestroyView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (abyss) {
            outState.putBoolean(ActionsAndIntents.ABYSS, abyss);
        }
        if (currentPage > 0) {
            outState.putInt(ActionsAndIntents.CURRENT_PAGE, currentPage);
        }
        if (nextPage != null) {
            outState.putString(ActionsAndIntents.NEXT_PAGE, nextPage);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, final View view, int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String[] items = getResources().getStringArray(R.array.item_menu);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                QuotesAdapter.ViewHolder viewHolder = (QuotesAdapter.ViewHolder) view.getTag();
                if (viewHolder != null) {
                    if (which == 0) {
                        getActivity().startService(new Intent(getActivity(), QuoteService.class).
                                putExtra(TYPE_ID, TYPE_RULEZ).putExtra(ActionsAndIntents.QUOTE_ID, viewHolder.publicId));
                    } else if (which == 1) {
                        getActivity().startService(new Intent(getActivity(), QuoteService.class).
                                putExtra(TYPE_ID, TYPE_SUX).putExtra(ActionsAndIntents.QUOTE_ID, viewHolder.publicId));
                    } else if (which == 2) {
                        dbHelper.addToFavorite(viewHolder.innerId);
                        Toast.makeText(getActivity(), R.string.added_to_favorites, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        builder.show();
        return true;
    }
}
