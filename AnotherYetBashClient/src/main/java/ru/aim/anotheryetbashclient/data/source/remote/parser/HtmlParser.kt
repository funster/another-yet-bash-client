package ru.aim.anotheryetbashclient.data.source.remote.parser

import ru.aim.anotheryetbashclient.data.Quote
import ru.aim.anotheryetbashclient.data.source.remote.NetworkRequestResult
import rx.Observable

interface HtmlParser<T> where T : Quote {

    fun parseQuotes(result: NetworkRequestResult): Observable<List<T>>

    fun parsePageCount(result: NetworkRequestResult): Observable<Int>
}