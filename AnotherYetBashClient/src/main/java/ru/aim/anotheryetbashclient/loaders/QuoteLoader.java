package ru.aim.anotheryetbashclient.loaders;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.http.AndroidHttpClient;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import ru.aim.anotheryetbashclient.BashApplication;
import ru.aim.anotheryetbashclient.R;
import ru.aim.anotheryetbashclient.helper.DbHelper;
import ru.aim.anotheryetbashclient.helper.Utils;

import static ru.aim.anotheryetbashclient.helper.DbHelper.QUOTE_PUBLIC_ID;
import static ru.aim.anotheryetbashclient.loaders.Package.getCharsetFromResponse;

@SuppressWarnings("unused")
public abstract class QuoteLoader extends AbstractLoader<Cursor> {

    public QuoteLoader(Context context) {
        super(context);
    }

    protected void onEachElement(Element e) {
        onEachElement(e, 0);
    }

    protected void onEachElement(Element e, int flag) {
        Elements idElements = selectId(e);
        Elements dateElements = selectDate(e);
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

    protected Elements selectDate(Element e) {
        return e.select("span[class=date]");
    }

    protected Elements selectId(Element e) {
        return e.select("a[class=id]");
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
        return Jsoup.parse(getInputStream(httpResponse), encoding, getUrl());
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
        HttpGet httpGet = new HttpGet(getUrl());
        AndroidHttpClient.modifyRequestToAcceptGzipResponse(httpGet);
        return httpGet;
    }

    public static InputStream getInputStream(HttpResponse httpResponse) throws IOException {
        InputStream inputStream = httpResponse.getEntity().getContent();
        Header contentEncoding = httpResponse.getFirstHeader("Content-Encoding");
        if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
            inputStream = new GZIPInputStream(inputStream);
        }
        return inputStream;
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
