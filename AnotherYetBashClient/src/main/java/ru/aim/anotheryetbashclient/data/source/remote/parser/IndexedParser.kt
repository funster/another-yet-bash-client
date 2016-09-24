package ru.aim.anotheryetbashclient.data.source.remote.parser

import org.jsoup.Jsoup
import ru.aim.anotheryetbashclient.data.Quote
import ru.aim.anotheryetbashclient.data.source.remote.NetworkRequestResult
import rx.Observable

class IndexedParser : AbstractParser<Quote.Indexed>() {

    override fun newInstance(): Quote.Indexed = Quote.Indexed()

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
}
