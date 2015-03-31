package ru.aim.anotheryetbashclient.async;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import ru.aim.anotheryetbashclient.helper.L;

public class AsyncActivity extends Activity implements IDataReceiver {

    private static final String TAG = "AsyncActivity";

    private AsyncManager asyncManager;

    BroadcastReceiver asyncServiceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            asyncManager.onIntentReceived(intent);
        }
    };

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        asyncManager = new AsyncActivityManager(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        asyncManager.onResume();
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        manager.registerReceiver(asyncServiceReceiver, new IntentFilter(AsyncService.TASK_FINISHED_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        manager.unregisterReceiver(asyncServiceReceiver);
    }

    @Override
    public void onData(ISimpleCommand command) {
        L.d(TAG, "On data received: " + command.toString());
    }

    @Override
    public void onError(ISimpleCommand command) {
        L.d(TAG, "On error received: " + command.toString());
    }

    public AsyncManager getAsyncManager() {
        return asyncManager;
    }
}
