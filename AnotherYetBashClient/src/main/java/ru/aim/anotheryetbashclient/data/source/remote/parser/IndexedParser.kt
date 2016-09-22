package ru.aim.anotheryetbashclient.data.source.remote.parser

import org.jsoup.Jsoup
import ru.aim.anotheryetbashclient.data.Quote
import ru.aim.anotheryetbashclient.data.source.remote.NetworkRequestResult
import rx.Observable
import java.util.*
import javax.inject.Singleton

@Singleton
class IndexedParser : HtmlParser<Quote.Indexed> {

    override fun parsePageCount(result: NetworkRequestResult): Observable<Int> {
        return Observable.create {
            try {
                val document = Jsoup.parse(result.stream, result.encoding, result.url)
                val quotesElements = document.select("span[class=current]")
                for (e in quotesElements) {
                    val input = e.child(0)
                    val max = input.attr("max")
                    if (max != null) {
                        it.onNext(Integer.parseInt(max))
                        break
                    }
                }
                it.onCompleted()
            } catch(t: Throwable) {
                it.onError(t)
            }
        }
    }

    override fun parseQuotes(result: NetworkRequestResult): Observable<List<Quote.Indexed>> {
        return Observable.create {
            try {
                val document = Jsoup.parse(result.stream, result.encoding, result.url)
                val quotesElements = document.select("div[class=quote]")
                val result = ArrayList<Quote.Indexed>()
                for (e in quotesElements) {
                    val idElements = e.select("a[class=id]")
                    val dateElements = e.select("span[class=date]")
                    val textElements = e.select("div[class=text]")
                    val ratingElements = e.select("span[class=rating]")
                    if (!textElements.isEmpty()) {
                        val tmp = Quote.Indexed()
                        tmp.text = textElements.html().trim { it <= ' ' }
                        tmp.publicId = idElements.html()
                        tmp.rating = ratingElements.html().trim { it <= ' ' }
                        tmp.date = dateElements.html()
                        tmp.isNew = true
                        result.add(tmp)
                    }
                }
                it.onNext(result)
                it.onCompleted()
            } catch(t: Throwable) {
                it.onError(t)
            }
        }
    }
}
