package ru.aim.anotheryetbashclient.support.delegate

import android.app.Activity
import android.os.Bundle

class DefaultActivityDelegate(override val activity: Activity) : ActivityDelegate {

    override fun onCreate(state: Bundle?) {
    }

    override fun onStart() {
    }

    override fun onResume() {
    }

    override fun onPause() {
    }

    override fun onStop() {
    }

    override fun onDestroy() {
    }
}