package ru.aim.anotheryetbashclient.loaders;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.aim.anotheryetbashclient.fragments.DateResult;

import static ru.aim.anotheryetbashclient.loaders.Package.wrapWithRoot;

/**
 *
 */
public class AbyssBestLoader extends QuoteLoader {

    static final String URL = wrapWithRoot("abyssbest");
    static final String MORE_URL = wrapWithRoot("abyssbest/%d%02d%02d");

    DateResult dateResult;

    public AbyssBestLoader(Context context, Bundle args) {
        super(context);
        dateResult = (DateResult) args.getSerializable("dateResult");
    }

    @Override
    protected String getUrl() {
        if (dateResult != null && !dateResult.isToday()) {
            return String.format(MORE_URL, dateResult.year, dateResult.month + 1, dateResult.day);
        } else {
            return URL;
        }
    }

    protected void saveQuote(ContentValues values) {
        getDbHelper().addQuoteToDefault(values);
    }

    @Override
    protected Elements selectId(Element e) {
        return e.select("span[class=id]");
    }
}
