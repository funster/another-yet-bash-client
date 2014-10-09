package ru.aim.anotheryetbashclient;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;

import java.util.Calendar;

import ru.aim.anotheryetbashclient.helper.DbHelper;
import ru.aim.anotheryetbashclient.helper.L;
import ru.aim.anotheryetbashclient.loaders.FreshLoader;

public class QuoteService extends IntentService {

    private static final String TAG = "QuoteService";

    public QuoteService() {
        super("QuoteService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        L.d(TAG, "Service raised");
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
            SettingsHelper.writeUpdateTimestamp(this, Calendar.getInstance().getTimeInMillis());
        } catch (Exception e) {
            L.e(TAG, "Error while getting new quotes", e);
        }
    }
}
