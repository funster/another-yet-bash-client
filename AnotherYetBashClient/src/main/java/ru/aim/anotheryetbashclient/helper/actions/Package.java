package ru.aim.anotheryetbashclient.helper.actions;

import org.apache.http.HttpResponse;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Common functions for {@code ru.aim.anotheryetbashclient.helper.actions} package.
 */
@SuppressWarnings("unused")
final class Package {

    static long OFFLINE_SLEEP = 1000L;

    private Package() {
        throw new AssertionError();
    }

    static String getCharsetFromResponse(HttpResponse httpResponse) {
        return httpResponse.getEntity().getContentType().getValue().split("=")[1];
    }

    static String findMore(Document document) {
        Elements refs = document.select("#body > div.quote.more > a");
        if (refs.size() > 0) {
            return refs.get(0).attr("href");
        } else {
            throw new AssertionError();
        }
    }
}
