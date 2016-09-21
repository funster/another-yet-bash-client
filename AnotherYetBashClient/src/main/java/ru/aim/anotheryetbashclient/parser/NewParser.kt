package ru.aim.anotheryetbashclient.parser

import org.jsoup.Jsoup
import ru.aim.anotheryetbashclient.data.Quote
import ru.aim.anotheryetbashclient.data.source.remote.NetworkRequestResult
import rx.Observable
import java.util.*

class NewParser : HtmlParser {

    override fun parse(result: NetworkRequestResult): List<Quote> {
        val document = Jsoup.parse(result.stream, result.encoding, result.url)
        val list = ArrayList<Quote>()
        val quotesElements = document.select("div[class=quote]")
        for (e in quotesElements) {
            val idElements = e.select("a[class=id]")
            val dateElements = e.select("span[class=date]")
            val textElements = e.select("div[class=text]")
            val ratingElements = e.select("span[class=rating]")
            if (!textElements.isEmpty()) {
                val tmp = Quote(
                        0,
                        textElements.html().trim { it <= ' ' },
                        idElements.html(),
                        ratingElements.html().trim { it <= ' ' },
                        dateElements.html(),
                        true,
                        0
                )
                list.add(tmp)
            }
        }
        return list
    }

    override fun parseAsync(result: NetworkRequestResult): Observable<Quote> {
        return Observable.create {
            try {
                val document = Jsoup.parse(result.stream, result.encoding, result.url)
                val quotesElements = document.select("div[class=quote]")
                for (e in quotesElements) {
                    val idElements = e.select("a[class=id]")
                    val dateElements = e.select("span[class=date]")
                    val textElements = e.select("div[class=text]")
                    val ratingElements = e.select("span[class=rating]")
                    if (!textElements.isEmpty()) {
                        val tmp = Quote(
                                0,
                                textElements.html().trim { it <= ' ' },
                                idElements.html(),
                                ratingElements.html().trim { it <= ' ' },
                                dateElements.html(),
                                true,
                                0
                        )
                        it.onNext(tmp)
                    }
                }
                it.onCompleted()
            } catch(t: Throwable) {
                it.onError(t)
            }
        }
    }
}
