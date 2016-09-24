package ru.aim.anotheryetbashclient.data.source.remote.parser

import org.jsoup.Jsoup
import ru.aim.anotheryetbashclient.data.Quote
import ru.aim.anotheryetbashclient.data.source.remote.NetworkRequestResult
import rx.Observable
import java.util.*

abstract class AbstractParser<T> : HtmlParser<T> where T : Quote {

    override fun parseQuotes(result: NetworkRequestResult): Observable<List<T>> {
        return Observable.create {
            try {
                val document = Jsoup.parse(result.stream, result.encoding, result.url)
                val quotesElements = document.select("div[class=quote]")
                val resultList = ArrayList<T>()
                for (e in quotesElements) {
                    val idElements = e.select("a[class=id]")
                    val dateElements = e.select("span[class=date]")
                    val textElements = e.select("div[class=text]")
                    val ratingElements = e.select("span[class=rating]")
                    if (!textElements.isEmpty()) {
                        val tmp = newInstance()
                        tmp.text = textElements.html().trim { it <= ' ' }
                        tmp.publicId = idElements.html()
                        tmp.rating = ratingElements.html().trim { it <= ' ' }
                        tmp.date = dateElements.html()
                        tmp.isNew = true
                        resultList.add(tmp)
                    }
                }
                it.onNext(resultList)
                it.onCompleted()
            } catch(t: Throwable) {
                it.onError(t)
            }
        }
    }

    protected abstract fun newInstance(): T
}