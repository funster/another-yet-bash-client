package ru.aim.anotheryetbashclient.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import ru.aim.anotheryetbashclient.ActionsAndIntents;
import ru.aim.anotheryetbashclient.QuotesAdapter;
import ru.aim.anotheryetbashclient.R;
import ru.aim.anotheryetbashclient.helper.DbHelper;
import ru.aim.anotheryetbashclient.helper.ObjectSerializer;
import ru.aim.anotheryetbashclient.helper.QuoteService;

import java.util.ArrayList;

import static ru.aim.anotheryetbashclient.ActionsAndIntents.*;

public class AbyssBashFragment extends AbstractBashFragment {

    int currentPage;
    Cursor cursor;

    void update() {
        nextPage();
    }

    void back() {
        currentPage += 1;
        nextPage();
    }

    void forward() {
        currentPage -= 1;
        nextPage();
    }

    void nextPage() {
        Intent intent = new Intent(getActivity(), QuoteService.class);
        intent.putExtra(TYPE_ID, ActionsAndIntents.TYPE_NEW);
        if (currentPage > 0) {
            intent.putExtra(ActionsAndIntents.CURRENT_PAGE, currentPage);
        }
        getActivity().startService(intent);
        setRefreshing(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        update();
    }

    boolean isSavedCursorExists() {
        return PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(CURRENT_QUOTES, null) == null;
    }

    @SuppressWarnings("unchecked")
    String[] getSavedCursor() {
        String bin = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(CURRENT_QUOTES, null);
        ArrayList<String> list = (ArrayList<String>) ObjectSerializer.deserialize(bin);
        return list.toArray(new String[list.size()]);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (currentPage > 0) {
            outState.putInt(ActionsAndIntents.CURRENT_PAGE, currentPage);
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
                        getDbHelper().addToFavorite(viewHolder.innerId);
                        Toast.makeText(getActivity(), R.string.added_to_favorites, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        builder.show();
        return true;
    }

    @Override
    public void onReceive(Intent intent) {
        ArrayList<String> list = intent.getStringArrayListExtra(ActionsAndIntents.IDS);
        assert list != null;
        if (!list.isEmpty()) {
            String[] arr = list.toArray(new String[list.size()]);
            if (cursor != null) {
                cursor.close();
            }
            cursor = getDbHelper().getQuotes(arr);
        }
        if (intent.hasExtra(ActionsAndIntents.CURRENT_PAGE)) {
            currentPage = intent.getIntExtra(ActionsAndIntents.CURRENT_PAGE, 0);
        }
        saveCurrentCursor(intent, cursor);
        setListAdapter(new QuotesAdapter(getDbHelper(), getActivity(), cursor));
        updateHeader();
        setRefreshing(false);
    }

    void saveCurrentCursor(Intent intent, Cursor cursor) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (cursor == null) {
            preferences.edit().remove(CURRENT_QUOTES).commit();
        } else {
            if (intent.hasExtra(ActionsAndIntents.ABYSS)) {
                preferences.edit().clear().putBoolean(ActionsAndIntents.ABYSS, true).commit();
            } else {
                ArrayList<String> list = new ArrayList<String>();
                while (cursor.moveToNext()) {
                    list.add(cursor.getString(cursor.getColumnIndex(DbHelper.QUOTE_PUBLIC_ID)));
                }
                String bin = ObjectSerializer.serialize(list);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.putString(CURRENT_QUOTES, bin);
                editor.commit();
            }
        }
    }

    @Override
    public void onManualUpdate() {
        update();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_forward) {
            forward();
            return true;
        }
        if (item.getItemId() == R.id.action_back) {
            back();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getType() {
        return ActionsAndIntents.TYPE_ABYSS;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.news, menu);
    }

    void updateHeader() {
        String[] types = getResources().getStringArray(R.array.types);
        String actionBarTitle;
        actionBarTitle = getResources().getString(R.string.app_name_with_type_and_page, types[ActionsAndIntents.TYPE_NEW],
                Integer.toString(currentPage));
        assert getActivity().getActionBar() != null;
        getActivity().getActionBar().setTitle(actionBarTitle);
    }
}
