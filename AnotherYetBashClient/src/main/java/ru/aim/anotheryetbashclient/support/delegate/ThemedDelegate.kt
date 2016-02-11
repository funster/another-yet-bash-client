package ru.aim.anotheryetbashclient.support.delegate

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import ru.aim.anotheryetbashclient.settings.SettingsHelper

class ThemedDelegate(val delegate: ResumedActionDelegate) : ActivityDelegate by delegate {

    internal var changeThemeReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            delegate.runEvenPaused({
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                    activity.finish()
                    activity.startActivity(activity.getIntent())
                } else {
                    activity.recreate()
                }
            })
        }
    }

    override fun onCreate(state: Bundle?) {
        val changedTheme = SettingsHelper.getTheme(activity)
        activity.setTheme(changedTheme)
        val manager = LocalBroadcastManager.getInstance(activity)
        manager.registerReceiver(changeThemeReceiver, IntentFilter(CHANGE_THEME_ACTION))
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(activity).unregisterReceiver(changeThemeReceiver)
    }
}