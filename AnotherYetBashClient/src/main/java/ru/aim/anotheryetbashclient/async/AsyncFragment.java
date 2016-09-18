package ru.aim.anotheryetbashclient.async;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;

import ru.aim.anotheryetbashclient.async.commands.ISimpleCommand;

import static ru.aim.anotheryetbashclient.helper.L.d;

@SuppressWarnings("unused")
public class AsyncFragment extends Fragment implements IDataReceiver {

    private static final String TAG = "AsyncFragment";

    protected AsyncManager asyncManager;

    BroadcastReceiver asyncServiceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            asyncManager.onIntentReceived(intent);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        asyncManager = new AsyncFragmentManager(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        asyncManager.onResume();
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getActivity());
        manager.registerReceiver(asyncServiceReceiver, new IntentFilter(AsyncService.TASK_FINISHED_ACTION));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getActivity());
        manager.unregisterReceiver(asyncServiceReceiver);
    }

    public AsyncManager getAsyncManager() {
        return asyncManager;
    }

    @Override
    public void onData(ISimpleCommand command) {
        d(TAG, "On data received: " + command.toString());
    }

    @Override
    public void onError(ISimpleCommand command) {
        d(TAG, "On error received: " + command.toString());
    }
}
