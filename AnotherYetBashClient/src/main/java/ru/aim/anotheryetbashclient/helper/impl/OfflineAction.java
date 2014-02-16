package ru.aim.anotheryetbashclient.helper.impl;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.content.LocalBroadcastManager;
import ru.aim.anotheryetbashclient.ActionsAndIntents;
import ru.aim.anotheryetbashclient.helper.BaseAction;
import ru.aim.anotheryetbashclient.helper.DbHelper;

import java.util.ArrayList;

/**
 *
 */
public class OfflineAction extends BaseAction {

    @Override
    public void apply() {
        ArrayList<String> list = new ArrayList<String>();
        Cursor cursor = getDbHelper().getUnread();
        while (cursor.moveToNext()) {
            list.add(cursor.getString(cursor.getColumnIndex(DbHelper.QUOTE_PUBLIC_ID)));
        }
        Intent intent = new Intent(ActionsAndIntents.REFRESH);
        intent.putStringArrayListExtra(ActionsAndIntents.IDS, list);
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getContext());
        manager.sendBroadcast(intent);
    }
}
