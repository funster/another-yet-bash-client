package ru.aim.anotheryetbashclient.support;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import ru.aim.anotheryetbashclient.BuildConfig;
import ru.aim.anotheryetbashclient.settings.SettingsHelper;

public abstract class ThemedActivity extends OrientationActivity {

    public static final String CHANGE_THEME_ACTION = BuildConfig.APPLICATION_ID + ".CHANGE_THEME_ACTION";

    BroadcastReceiver changeThemeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            submitAction(() -> {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                    finish();
                    startActivity(getIntent());
                } else {
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
