package ru.aim.anotheryetbashclient.helper;

import android.app.IntentService;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import org.apache.http.client.params.HttpClientParams;
import ru.aim.anotheryetbashclient.ActionsAndIntents;
import ru.aim.anotheryetbashclient.R;
import ru.aim.anotheryetbashclient.helper.f.Action;

import static ru.aim.anotheryetbashclient.helper.ActionRequestFactory.getQuiteRequest;
import static ru.aim.anotheryetbashclient.helper.Utils.isNetworkAvailable;
import static ru.aim.anotheryetbashclient.helper.Utils.sendMessageIntent;

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
                    httpClient = AndroidHttpClient.newInstance(System.getProperty("http.agent", "Android"));
                    HttpClientParams.setRedirecting(httpClient.getParams(), true);
                    httpClient.enableCurlLogging(TAG, Log.INFO);
                }
            }
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        DbHelper dbHelper;
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        if (isNetworkAvailable(this)) {
            try {
                checkIfClientNotCreated();
                Action action = getQuiteRequest(intent);
                if (action instanceof HttpAware) {
                    ((HttpAware) action).setHttpClient(httpClient);
                }
                if (action instanceof ContextAware) {
                    ((ContextAware) action).setContext(this);
                }
                if (action instanceof DbHelperAware) {
                    dbHelper = new DbHelper(this);
                    ((DbHelperAware) action).setDbHelper(dbHelper);
                }
                if (action instanceof IntentAware) {
                    ((IntentAware) action).setIntent(intent);
                }
                action.apply();
            } catch (Exception e) {
                L.d(TAG, "Error while getting new quotes", e);
                sendMessageIntent(this, getString(R.string.updating_fail));
                localBroadcastManager.sendBroadcast(new Intent(ActionsAndIntents.REFRESH));
            }
        } else {
            L.d(TAG, "Not internet connection");
            localBroadcastManager.sendBroadcast(new Intent(ActionsAndIntents.REFRESH));
        }
    }
}