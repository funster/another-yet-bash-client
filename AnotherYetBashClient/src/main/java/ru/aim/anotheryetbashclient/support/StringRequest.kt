package ru.aim.anotheryetbashclient.support

import android.os.SystemClock
import com.octo.android.robospice.request.SpiceRequest
import java.util.concurrent.atomic.AtomicInteger

class StringRequest() : SpiceRequest<String>(String::class.java) {

    companion object {
        val counter = AtomicInteger(0)
    }

    override fun loadDataFromNetwork(): String? {
        SystemClock.sleep(3000)
        return "counter:${counter.incrementAndGet()}"
    }
}