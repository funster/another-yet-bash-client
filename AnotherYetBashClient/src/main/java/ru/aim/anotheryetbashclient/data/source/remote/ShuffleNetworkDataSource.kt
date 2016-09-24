package ru.aim.anotheryetbashclient.data.source.remote

import ru.aim.anotheryetbashclient.data.Quote
import ru.aim.anotheryetbashclient.data.source.remote.parser.IndexedParser
import rx.Observable
import javax.inject.Inject

class ShuffleNetworkDataSource @Inject constructor(private val networkClient: NetworkClient, private val parser: IndexedParser) {

    private val rootPage = wrapWithRoot("random")

    private var lastRandom = ""

    fun getShuffle(): Observable<List<Quote>> = Observable.fromCallable { networkClient.getNetworkRequest(rootPage) }.flatMap { parser.parseQuotes(it) }
}