package ru.pavelshackih.anotheryetbashclient.loaders;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.http.AndroidHttpClient;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import ru.pavelshackih.anotheryetbashclient.BashApplication;
import ru.pavelshackih.anotheryetbashclient.R;
import ru.pavelshackih.anotheryetbashclient.helper.DbHelper;
import ru.pavelshackih.anotheryetbashclient.helper.Utils;

import static ru.pavelshackih.anotheryetbashclient.helper.DbHelper.QUOTE_PUBLIC_ID;
import static ru.pavelshackih.anotheryetbashclient.loaders.Package.getCharsetFromResponse;

@SuppressWarnings("unused")
public abstract class QuoteLoader extends AbstractLoader<Cursor> {

    public QuoteLoader(Context context) {
        super(context);
    }

    protected void onEachElement(Element e) {
        onEachElement(e, 0);
    }

    protected void onEachElement(Element e, int flag) {
        Elements idElements = e.select("a[class=id]");
        Elements dateElements = e.select("span[class=date]");
        Elements textElements = e.select("div[class=text]");
        Elements ratingElements = e.select("span[class=rating]");
        if (!textElements.isEmpty()) {
            ContentValues values = new ContentValues();
            values.put(QUOTE_PUBLIC_ID, idElements.html());
            values.put(DbHelper.QUOTE_DATE, dateElements.html());
            values.put(DbHelper.QUOTE_IS_NEW, 1);
            values.put(DbHelper.QUOTE_TEXT, textElements.html().trim());
            values.put(DbHelper.QUOTE_RATING, ratingElements.html().trim());
            values.put(DbHelper.QUOTE_FLAG, flag);
            saveQuote(values);
        }
    }

    @Override
    public Cursor doInBackground() throws Exception {
        if (Utils.isNetworkNotAvailable(getContext())) {
            throw new RuntimeException(getContext().getString(R.string.error_no_connection));
        }
        Document document = prepareRequest();
        beforeParsing(document);
        Elements quotesElements = document.select("div[class=quote]");
        for (Element e : quotesElements) {
            onEachElement(e);
        }
        afterParsing();
        return getDbHelper().selectFromDefaultTable();
    }

    protected Document prepareRequest() throws IOException {
        HttpUriRequest httpRequest = getHttpRequest();
        HttpResponse httpResponse = getHttpClient().execute(httpRequest);
        String encoding = getCharsetFromResponse(httpResponse);
        return Jsoup.parse(httpResponse.getEntity().getContent(), encoding, getUrl());
    }

    protected void saveQuote(ContentValues values) {
        if (getDbHelper().notExists(values.getAsString(DbHelper.QUOTE_PUBLIC_ID))) {
            getDbHelper().addQuoteToDefault(values);
        }
    }

    protected void beforeParsing(Document document) {
        getDbHelper().clearDefault();
    }

    protected HttpUriRequest getHttpRequest() {
        return new HttpGet(getUrl());
    }

    protected abstract String getUrl();

    protected void afterParsing() {
    }

    public AndroidHttpClient getHttpClient() {
        if (getContext() == null) {
            return null;
        }
        BashApplication app = (BashApplication) getContext().getApplicationContext();
        return app.getHttpClient();
    }
}
