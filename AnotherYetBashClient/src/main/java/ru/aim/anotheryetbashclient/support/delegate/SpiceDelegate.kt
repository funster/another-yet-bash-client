package ru.aim.anotheryetbashclient.support.delegate

import com.octo.android.robospice.SpiceManager
import ru.aim.anotheryetbashclient.async.NetworkService

class SpiceDelegate(val delegate: ActivityDelegate) : ActivityDelegate by delegate {

    val spiceManager = SpiceManager(NetworkService::class.java)

    override fun onStart() {
        spiceManager.start(activity)
    }

    override fun onStop() {
        if (spiceManager.isStarted) {
            spiceManager.shouldStop()
        }
    }
}