package ru.aim.anotheryetbashclient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

public abstract class ThemedActivity extends OrientationActivity {

    public static final String CHANGE_THEME_ACTION = "ru.aim.anotheryetbashclient.CHANGE_THEME_ACTION";

    BroadcastReceiver changeThemeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            submitAction(new Runnable() {
                @Override
                public void run() {
                    recreate();
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int changedTheme = SettingsHelper.getTheme(this);
        setTheme(changedTheme);
        super.onCreate(savedInstanceState);
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        manager.registerReceiver(changeThemeReceiver, new IntentFilter(CHANGE_THEME_ACTION));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(changeThemeReceiver);
    }

    public static void sendChangeTheme(Context context) {
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(context);
        manager.sendBroadcast(new Intent(CHANGE_THEME_ACTION));
    }
}
