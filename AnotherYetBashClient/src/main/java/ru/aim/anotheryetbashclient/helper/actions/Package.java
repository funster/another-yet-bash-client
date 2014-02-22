package ru.aim.anotheryetbashclient.helper.actions;

import android.content.ContentValues;
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

    static long OFFLINE_SLEEP = 2000L;

    private Package() {
        throw new AssertionError();
    }

    static void parseDocument(final InputStream is, final String uri, final Function1<ElementWrapper, Void> f) {
        rethrowWithRuntime(new Block() {
            @Override
            public void apply() throws Exception {
                Document document = Jsoup.parse(is, WINDOWS_1215, uri);
                Elements quotesElements = document.select("div[class=quote]");
                for (Element e : quotesElements) {
                    Elements idElements = e.select("a[class=id]");
                    Elements dateElements = e.select("span[class=date]");
                    Elements textElements = e.select("div[class=text]");
                    if (!textElements.isEmpty()) {
                        f.apply(new ElementWrapper(idElements.html(), dateElements.html(), textElements.html().trim()));
                    }
                }
            }
        });
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
