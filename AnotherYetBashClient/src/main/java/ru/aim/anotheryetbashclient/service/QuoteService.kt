package ru.aim.anotheryetbashclient.service

import okhttp3.Request
import ru.aim.anotheryetbashclient.data.local.QuoteDao
import ru.aim.anotheryetbashclient.data.remote.*
import ru.aim.anotheryetbashclient.parser.HtmlParser
import rx.Observable

class QuoteService(val dao: QuoteDao, val parser: HtmlParser) {

    private val TAG = "QuoteService"

    fun loadAndSaveAndGet() = Observable.fromCallable { loadFirstPage() }.
            map { parser.parse(it) }

    fun loadFirstPage(): NetworkRequestResult {
        val builder = Request.Builder()
        val url = wrapWithRoot()
        builder.get().url(url)
        val response = networkClient.newCall(builder.build()).execute()
        return NetworkRequestResult(response.data, url, response.encoding)
    }

    fun findAll() = dao.findAll()
}