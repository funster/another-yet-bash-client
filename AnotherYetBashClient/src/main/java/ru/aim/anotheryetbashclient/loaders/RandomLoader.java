package ru.aim.anotheryetbashclient.loaders;

import android.content.Context;
import android.text.TextUtils;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import ru.aim.anotheryetbashclient.ActionsAndIntents;

import static ru.aim.anotheryetbashclient.loaders.Package.wrapWithRoot;
import static ru.aim.anotheryetbashclient.loaders.Package.wrapWithRootWithoutSlash;

public class RandomLoader extends QuoteLoader {

    public static int ID = ActionsAndIntents.TYPE_RANDOM;

    static final String URL = wrapWithRoot("random");
    static final String MORE_URL = wrapWithRootWithoutSlash("%s");

    String more;

    public RandomLoader(Context context) {
        super(context);
    }

    @Override
    protected String getUrl() {
        if (!TextUtils.isEmpty(more)) {
            return String.format(MORE_URL, more);
        } else {
            return URL;
        }
    }

    @Override
    protected void beforeParsing(Document document) {
        super.beforeParsing(document);
        more = findMore(document);
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
