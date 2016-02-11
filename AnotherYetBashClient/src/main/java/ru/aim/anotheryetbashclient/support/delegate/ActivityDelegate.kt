package ru.aim.anotheryetbashclient.support.delegate

import android.app.Activity
import android.os.Bundle

interface ActivityDelegate {

    fun onCreate(state: Bundle?)

    fun onStart()

    fun onResume()

    fun onPause()

    fun onStop()

    fun onDestroy()

    val activity: Activity
}