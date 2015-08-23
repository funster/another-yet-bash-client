package ru.aim.anotheryetbashclient;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import ru.aim.anotheryetbashclient.helper.L;
import ru.aim.anotheryetbashclient.helper.Utils;
import ru.aim.anotheryetbashclient.settings.SettingsHelper;

@Deprecated
public class QuoteService extends IntentService {

    private static final String TAG = "QuoteService";

    public QuoteService() {
        super("QuoteService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        L.d(TAG, "Service rises");
        if (Utils.isNetworkNotAvailable(this)) {
            L.d(TAG, "Network not available, so exit now");
            return;
        }
        boolean onlyByWifi = SettingsHelper.isUpdateOnlyByWifi(this);
        if (onlyByWifi) {
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
    }
}
