package ru.aim.anotheryetbashclient.data.source.remote

import ru.aim.anotheryetbashclient.data.Quote
import ru.aim.anotheryetbashclient.data.source.remote.parser.IndexedParser
import rx.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IndexedNetworkDataSource @Inject constructor(private val networkClient: NetworkClient, private val parser: IndexedParser) {

    private val rootPage = wrapWithRoot()
    private val nextPage = wrapWithRoot("index/%s")

    fun getPageCount(): Observable<Int> = Observable.fromCallable { networkClient.getNetworkRequest(wrapWithRoot()) }.flatMap { parser.parsePageCount(it) }

    fun getQuotesByPage(page: Int = 0): Observable<List<Quote>> =
            Observable.fromCallable {
                networkClient.getNetworkRequest(if (page == 0) rootPage else nextPage.format(page))
            }.flatMap { parser.parseQuotes(it) }
}