package ru.aim.anotheryetbashclient.helper;

import android.app.IntentService;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.http.AndroidHttpClient;
import android.support.v4.content.LocalBroadcastManager;

import java.io.IOException;

import ru.aim.anotheryetbashclient.ActionsAndIntents;

import static ru.aim.anotheryetbashclient.helper.QuiteRequestFactory.getQuiteRequest;
import static ru.aim.anotheryetbashclient.helper.Utils.isNetworkAvailable;

@SuppressWarnings("unused")
public class QuoteService extends IntentService {

    static final String TAG = "QuoteService";
    static volatile AndroidHttpClient httpClient;

    public QuoteService() {
        super("QuoteService");
    }

    static void checkIfClientNotCreated() {
        if (httpClient == null) {
            synchronized (QuoteService.class) {
                if (httpClient == null) {
                    httpClient = AndroidHttpClient.newInstance("Android");
                }
            }
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        DbHelper dbHelper = null;
        SQLiteDatabase database = null;
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        if (isNetworkAvailable(this)) {
            try {
                checkIfClientNotCreated();
                dbHelper = new DbHelper(this);
                database = dbHelper.getWritableDatabase();
                QuiteRequest quiteRequest = getQuiteRequest(intent);
                quiteRequest.doRequest(database, httpClient);
                localBroadcastManager.sendBroadcast(new Intent(ActionsAndIntents.REFRESH));
            } catch (IOException e) {
                L.d(TAG, "Error while getting new quotes", e);
            } catch (Exception e) {
                L.d(TAG, "Error while getting new quotes", e);
            } finally {
                if (database != null) {
                    database.close();
                }
                if (dbHelper != null) {
                    dbHelper.close();
                }
            }
        } else {
            L.d(TAG, "Not internet connection");
            localBroadcastManager.sendBroadcast(new Intent(ActionsAndIntents.REFRESH));
        }
    }
}