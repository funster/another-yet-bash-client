package ru.aim.anotheryetbashclient.support.delegate

import java.util.*

class ResumedActionDelegate(val delegate: OnScreenDelegate) : ActivityDelegate by delegate {

    private val actions = ArrayList<() -> Unit>()

    fun runEvenPaused(f: () -> Unit) {
        if (delegate.isResumed) {
            f.invoke()
        } else {
            actions.add(f)
        }
    }

    override fun onResume() {
        if (actions.isNotEmpty()) {
            actions.forEach { it.invoke() }
            actions.clear()
        }
    }

    override fun onDestroy() {
        actions.clear()
    }
}