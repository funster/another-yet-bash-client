package ru.aim.anotheryetbashclient.support.delegate

import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import ru.aim.anotheryetbashclient.BuildConfig

const val CHANGE_THEME_ACTION = "${BuildConfig.APPLICATION_ID}.CHANGE_THEME_ACTION"

fun sendChangeTheme(context: Context) {
    LocalBroadcastManager.getInstance(context)?.sendBroadcast(Intent(CHANGE_THEME_ACTION))
}