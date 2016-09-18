package ru.aim.anotheryetbashclient.parser

import android.os.SystemClock
import android.util.Log
import rx.Observable
import java.util.concurrent.TimeUnit

class NewParserTest : BaseRawFileTestCase() {

    var parser = NewParser()

    fun testParse() {
//        val observer = parser.parse(NetworkRequestResult(getFile(R.raw.new_page), "windows-1251", "http://bash.im/"))
//        val testSubscriber = TestSubscriber<Quote>()

        val subscription = Observable.create<Int> {
            it.onNext(1)
            it.onNext(2)
            it.onNext(2)
            it.onNext(2)
            it.onNext(2)
            SystemClock.sleep(4000)
            it.onNext(3)
            it.onNext(4)
            SystemClock.sleep(4000)
            it.onNext(5)
            it.onCompleted()
        }
                .window(3, TimeUnit.SECONDS)
                .subscribe {
                    t ->
                    Log.d("RX!", "On new")
                    t?.forEach {
                        Log.d("RX!", "On $it")
                    }
                }
//        observer.subscribe(testSubscriber)
//        testSubscriber.assertCompleted()
//        testSubscriber.assertNoErrors()
//        testSubscriber.assertValueCount(50)
    }
}