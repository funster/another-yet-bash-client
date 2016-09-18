package ru.aim.anotheryetbashclient.support

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import ru.aim.anotheryetbashclient.support.delegate.*

abstract class DelegateActivity : AppCompatActivity() {

    private val aggregator = AggregatorDelegate(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        initAggregator()
        aggregator.onCreate(savedInstanceState)
        super.onCreate(savedInstanceState)
    }

    private fun initAggregator() {
        // configure delegates
        val defaultDelegate = DefaultActivityDelegate(this)
        val onScreenDelegate = OnScreenDelegate(defaultDelegate)
        val resumedActionDelegate = ResumedActionDelegate(onScreenDelegate)

        // aggregator.addDelegate(defaultDelegate)
        aggregator.addDelegate(onScreenDelegate)
        aggregator.addDelegate(resumedActionDelegate)
        aggregator.addDelegate(ThemedDelegate(resumedActionDelegate))
        aggregator.addDelegate(LockOrientationDelegate(defaultDelegate))
    }

    override fun onStart() {
        aggregator.onStart()
        super.onStart()
    }

    override fun onResume() {
        aggregator.onResume()
        super.onResume()
    }

    override fun onPause() {
        aggregator.onPause()
        super.onPause()
    }

    override fun onStop() {
        aggregator.onStop()
        super.onStop()
    }

    override fun onDestroy() {
        aggregator.onDestroy()
        super.onDestroy()
    }

    private fun <T : ActivityDelegate> getDelegate(clazz: Class<T>): T? = aggregator.findDelegateByType(clazz)

    // extensions goes here ->

    fun DelegateActivity.runEvenPaused(f: () -> Unit) {
        getDelegate(ResumedActionDelegate::class.java)?.runEvenPaused { f() }
    }

    val DelegateActivity.isResumed: Boolean
        get() = getDelegate(OnScreenDelegate::class.java)?.isResumed ?: false
}