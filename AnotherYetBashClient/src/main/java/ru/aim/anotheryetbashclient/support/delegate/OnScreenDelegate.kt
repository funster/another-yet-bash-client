package ru.aim.anotheryetbashclient.support.delegate

import ru.aim.anotheryetbashclient.support.DelegateActivity

class OnScreenDelegate(val delegate: ActivityDelegate) : ActivityDelegate by delegate {

    var isResumed = false
        private set

    override fun onResume() {
        isResumed = true
    }

    override fun onPause() {
        isResumed = false
    }
}