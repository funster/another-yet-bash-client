package ru.pavelshackih.anotheryetbashclient;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import java.util.Calendar;

import ru.pavelshackih.anotheryetbashclient.helper.DbHelper;
import ru.pavelshackih.anotheryetbashclient.helper.L;
import ru.pavelshackih.anotheryetbashclient.loaders.FreshLoader;

public class QuoteService extends IntentService {

    private static final String TAG = "QuoteService";

    public QuoteService() {
        super("QuoteService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        L.d(TAG, "Service rises");
        boolean onlyWifi = SettingsHelper.isUpdateOnlyWifiEnabled(this);

        if (onlyWifi) {
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (networkInfo == null) {
                L.d(TAG, "Wifi not connected!1");
                return;
            }
            boolean isWifiConn = networkInfo.isConnected();
            if (!isWifiConn) {
                L.d(TAG, "Wifi not connected!2");
                return;
            }
        }

        DbHelper dbHelper = new DbHelper(this);
        dbHelper.clearFresh();
        FreshLoader freshLoader = new FreshLoader(this, Bundle.EMPTY) {
            @Override
            protected void addQuote(ContentValues contentValues) {
                getDbHelper().addQuoteToFresh(contentValues);
            }
        };
        freshLoader.setDbHelper(dbHelper);
        freshLoader.setFromService(true);
        try {
            freshLoader.doInBackground();
            SettingsHelper.writeTimestamp(this, Calendar.getInstance().getTimeInMillis());
        } catch (Exception e) {
            L.e(TAG, "Error while getting new quotes", e);
        }
    }
}
