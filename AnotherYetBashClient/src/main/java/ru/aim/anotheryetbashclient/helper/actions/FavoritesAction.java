package ru.aim.anotheryetbashclient.helper.actions;

import android.content.Intent;
import android.database.Cursor;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
import ru.aim.anotheryetbashclient.ActionsAndIntents;
import ru.aim.anotheryetbashclient.helper.BaseAction;
import ru.aim.anotheryetbashclient.helper.DbHelper;

import java.util.ArrayList;

import static ru.aim.anotheryetbashclient.helper.actions.Package.OFFLINE_SLEEP;

/**
 *
 */
public class FavoritesAction extends BaseAction {

    @Override
    public void apply() {
        SystemClock.sleep(OFFLINE_SLEEP);
        Cursor cursor = getDbHelper().getFavorites();
        ArrayList<String> list = new ArrayList<String>();
        while (cursor.moveToNext()) {
            list.add(cursor.getString(cursor.getColumnIndex(DbHelper.QUOTE_PUBLIC_ID)));
        }
        Intent intent = new Intent(ActionsAndIntents.REFRESH);
        intent.putStringArrayListExtra(ActionsAndIntents.IDS, list);
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.sendBroadcast(intent);
    }
}
