package ru.aim.anotheryetbashclient.support.delegate

import android.app.Activity
import android.os.Bundle
import java.util.*

class AggregatorDelegate(override val activity: Activity) : ActivityDelegate {

    private val delegates = ArrayList<ActivityDelegate>()

    override fun onCreate(state: Bundle?) {
        delegates.forEach { it.onCreate(state) }
    }

    override fun onStart() {
        delegates.forEach { it.onStart() }
    }

    override fun onResume() {
        delegates.forEach { it.onResume() }
    }

    override fun onPause() {
        delegates.forEach { it.onPause() }
    }

    override fun onStop() {
        delegates.forEach { it.onStop() }
    }

    override fun onDestroy() {
        delegates.forEach { it.onDestroy() }
        delegates.clear()
    }

    fun addDelegate(delegate: ActivityDelegate) {
        delegates.add(delegate)
    }

    fun <T> findDelegateByType(type: Class<T>): T? {
        val delegate = delegates.find { it.javaClass.equals(type) }
        @Suppress("UNCHECKED_CAST")
        return delegate as T
    }
}