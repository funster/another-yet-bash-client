package ru.aim.anotheryetbashclient.data.source.remote

import org.junit.Test
import ru.aim.anotheryetbashclient.data.Quote
import ru.aim.anotheryetbashclient.data.source.remote.parser.IndexedParser
import rx.observers.TestSubscriber

class NetworkDataSourceTest {

    val dataSource = IndexedNetworkDataSource(SimpleNetworkClient(), IndexedParser())

    @Test
    fun getPageCount() {
        val obs = dataSource.getPageCount()
        val testSubscriber = TestSubscriber<Int>()
        obs.subscribe(testSubscriber)

        testSubscriber.assertNoErrors()
        val chickens = testSubscriber.onNextEvents
        println(chickens)
    }

    @Test
    fun getQuotesByPage() {
        val obs = dataSource.getQuotesByPage(500)
        val testSubscriber = TestSubscriber<List<Quote>>()
        obs.subscribe(testSubscriber)

        testSubscriber.assertNoErrors()
        val chickens = testSubscriber.onNextEvents
        println(chickens)
    }
}