package ru.aim.anotheryetbashclient.helper.actions;

import android.content.ContentValues;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.aim.anotheryetbashclient.helper.DbHelper;
import ru.aim.anotheryetbashclient.helper.f.Block;
import ru.aim.anotheryetbashclient.helper.f.Function1;
import ru.aim.anotheryetbashclient.helper.f.Tuple3;

import java.io.InputStream;

import static ru.aim.anotheryetbashclient.helper.DbHelper.QUOTE_PUBLIC_ID;
import static ru.aim.anotheryetbashclient.helper.Utils.WINDOWS_1215;
import static ru.aim.anotheryetbashclient.helper.Utils.rethrowWithRuntime;

/**
 * Common functions for {@code ru.aim.anotheryetbashclient.helper.actions} package.
 */
@SuppressWarnings("unused")
final class Package {

    static long OFFLINE_SLEEP = 1000L;

    private Package() {
        throw new AssertionError();
    }

    static void parseDocument(final InputStream is, final String uri, final Function1<ElementWrapper, Void> f) {
        parseDocument(is, uri, null, f);
    }

    static void parseDocument(final InputStream is, final String uri,
                              final Function1<Document, Void> f0,
                              final Function1<ElementWrapper, Void> f1) {
        rethrowWithRuntime(new Block() {
            @Override
            public void apply() throws Exception {
                Document document = Jsoup.parse(is, WINDOWS_1215, uri);
                if (f0 != null) {
                    f0.apply(document);
                }
                if (f1 != null) {
                    Elements quotesElements = document.select("div[class=quote]");
                    for (Element e : quotesElements) {
                        Elements idElements = e.select("a[class=id]");
                        Elements dateElements = e.select("span[class=date]");
                        Elements textElements = e.select("div[class=text]");
                        if (!textElements.isEmpty()) {
                            f1.apply(new ElementWrapper(idElements.html(), dateElements.html(), textElements.html().trim()));
                        }
                    }
                }
            }
        });
    }

    static void parseDocument(final HttpClient httpClient, final String uri, final Function1<Document, Void> f0,
                              final Function1<ElementWrapper, Void> f1) {
        rethrowWithRuntime(new Block() {
            @Override
            public void apply() throws Exception {
                HttpGet httpGet = new HttpGet(uri);
                HttpResponse httpResponse = httpClient.execute(httpGet);
                parseDocument(httpResponse.getEntity().getContent(), uri, f0, f1);
            }
        });
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

    static void storeInDb(DbHelper dbHelper, ElementWrapper elementWrapper) {
        if (dbHelper.notExists(elementWrapper.first)) {
            ContentValues values = new ContentValues();
            values.put(QUOTE_PUBLIC_ID, elementWrapper.first);
            values.put(DbHelper.QUOTE_DATE, elementWrapper.second);
            values.put(DbHelper.QUOTE_IS_NEW, 1);
            values.put(DbHelper.QUOTE_TEXT, elementWrapper.third);
            dbHelper.addNewQuote(values);
        }
    }

    static final class ElementWrapper extends Tuple3<String, String, String> {

        public ElementWrapper(String first, String second, String third) {
            super(first, second, third);
        }
    }
}
