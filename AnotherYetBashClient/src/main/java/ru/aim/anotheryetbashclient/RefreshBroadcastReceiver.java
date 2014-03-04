package ru.aim.anotheryetbashclient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import ru.aim.anotheryetbashclient.helper.DbHelper;
import ru.aim.anotheryetbashclient.helper.ObjectSerializer;

import java.util.ArrayList;

import static ru.aim.anotheryetbashclient.ActionsAndIntents.CURRENT_QUOTES;

/**
 *
 */
class RefreshBroadcastReceiver extends BroadcastReceiver {

    QuotesFragment fragment;

    public RefreshBroadcastReceiver(QuotesFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Cursor cursor = null;
        if (intent.hasExtra(ActionsAndIntents.ABYSS)) {
            fragment.abyss = true;
            cursor = fragment.dbHelper.getAbyss();
        } else {
            fragment.abyss = true;
            if (intent.hasExtra(ActionsAndIntents.IDS)) {
                ArrayList<String> list = intent.getStringArrayListExtra(ActionsAndIntents.IDS);
                assert list != null;
                if (!list.isEmpty()) {
                    String[] arr = list.toArray(new String[list.size()]);
                    cursor = fragment.dbHelper.getQuotes(arr);
                }
            } else {
                cursor = fragment.dbHelper.getUnread();
            }
            if (intent.hasExtra(ActionsAndIntents.CURRENT_PAGE)) {
                fragment.currentPage = intent.getIntExtra(ActionsAndIntents.CURRENT_PAGE, 0);
            }
            if (intent.hasExtra(ActionsAndIntents.NEXT_PAGE)) {
                fragment.nextPage = intent.getStringExtra(ActionsAndIntents.NEXT_PAGE);
            } else {
                fragment.nextPage = null;
            }
        }
        saveCurrentCursor(intent, cursor);
        fragment.listView.setAdapter(new QuotesAdapter(fragment.dbHelper, context, cursor));
        fragment.getActivity().setProgressBarIndeterminateVisibility(false);
    }

    void saveCurrentCursor(Intent intent, Cursor cursor) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(fragment.getActivity());
        if (cursor == null) {
            preferences.edit().remove(CURRENT_QUOTES).commit();
        } else {
            if (intent.hasExtra(ActionsAndIntents.ABYSS)) {
                preferences.edit().clear().putBoolean(ActionsAndIntents.ABYSS, true).commit();
            } else {
                ArrayList<String> list = new ArrayList<>();
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
}
