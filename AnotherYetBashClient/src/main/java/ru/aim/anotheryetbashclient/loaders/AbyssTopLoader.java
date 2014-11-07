package ru.aim.anotheryetbashclient.loaders;

import android.content.ContentValues;
import android.content.Context;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.aim.anotheryetbashclient.ActionsAndIntents;

import static ru.aim.anotheryetbashclient.loaders.Package.wrapWithRoot;

/**
 *
 */
public class AbyssTopLoader extends QuoteLoader {

    public static final int ID = ActionsAndIntents.TYPE_TOP_ABYSS;

    static final String URL = wrapWithRoot("abysstop");

    public AbyssTopLoader(Context context) {
        super(context);
    }

    @Override
    protected String getUrl() {
        return URL;
    }

    protected void saveQuote(ContentValues values) {
        getDbHelper().addQuoteToDefault(values);
    }

    @Override
    protected Elements selectDate(Element e) {
        return e.select("span[class=abysstop-date]");
    }
}
