package ru.aim.anotheryetbashclient.parser

import ru.aim.anotheryetbashclient.data.Quote
import ru.aim.anotheryetbashclient.data.source.remote.NetworkRequestResult
import rx.Observable

interface HtmlParser {

    fun parse(result: NetworkRequestResult): List<Quote>

    fun parseAsync(result: NetworkRequestResult): Observable<Quote>
}