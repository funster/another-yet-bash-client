package ru.aim.anotheryetbashclient.loaders;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.aim.anotheryetbashclient.ActionsAndIntents;
import ru.aim.anotheryetbashclient.fragments.DateResult;

import static ru.aim.anotheryetbashclient.loaders.Package.wrapWithRoot;

public class BestLoader extends QuoteLoader {

    public static int ID = ActionsAndIntents.TYPE_BEST;

    public static final String URL = wrapWithRoot("best");
    public static final String BEST_YEAR = wrapWithRoot("bestyear/%s");
    public static final String BEST_MONTH = wrapWithRoot("bestmonth/%s/%s");

    String year;
    String month;

    public BestLoader(Context context, Bundle bundle) {
        super(context);
        DateResult dateResult = (DateResult) bundle.getSerializable("dateResult");
        if (dateResult != null && !dateResult.isToday()) {
            year = Integer.toString(dateResult.year);
            month = Integer.toString(dateResult.month);
        }
    }

    @Override
    public Cursor doInBackground() throws Exception {
        if (isFirstPage()) {
            Document document = prepareRequest();
            beforeParsing(document);
            Elements elements = document.select("div[id=body]").get(0).children();
            int flag = 0;
            for (Element element : elements) {
                if (element.html().contains("недели")) {
                    flag = 1;
                }
                if (element.hasClass("quote")) {
                    onEachElement(element, flag);
                }
            }
            return getDbHelper().selectFromDefaultTable();
        } else {
            return super.doInBackground();
        }
    }

    @Override
    protected void saveQuote(ContentValues values) {
        getDbHelper().addQuoteToDefault(values);
    }

    boolean isFirstPage() {
        return year == null;
    }

    @Override
    protected String getUrl() {
        if (isFirstPage()) {
            return URL;
        } else {
            if (month == null) {
                return String.format(BEST_YEAR, year);
            } else {
                return String.format(BEST_MONTH, year, month);
            }
        }
    }
}