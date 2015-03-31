package ru.aim.anotheryetbashclient.tasks;

import android.content.ContentValues;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import ru.aim.anotheryetbashclient.R;
import ru.aim.anotheryetbashclient.helper.DbHelper;

import static ru.aim.anotheryetbashclient.helper.DbHelper.QUOTE_PUBLIC_ID;
import static ru.aim.anotheryetbashclient.helper.Utils.isNetworkNotAvailable;
import static ru.aim.anotheryetbashclient.loaders.Package.getCharsetFromResponse;
import static ru.aim.anotheryetbashclient.tasks.Package.getHttpRequest;
import static ru.aim.anotheryetbashclient.tasks.Package.getInputStream;

public abstract class TemplateTask extends BaseTask {

    @Override
    public final void execute() throws Exception {
        if (isNetworkNotAvailable(getContext())) {
            throw new RuntimeException(getContext().getString(R.string.error_no_connection));
        }
        Document document = prepareRequest();
        beforeParsing(document);
        Elements quotesElements = document.select("div[class=quote]");
        for (Element e : quotesElements) {
            onEachElement(e);
        }
        afterParsing();
    }

    protected abstract String getUrl();

    protected void saveQuote(ContentValues values) {
        if (getDbHelper().notExists(values.getAsString(DbHelper.QUOTE_PUBLIC_ID))) {
            getDbHelper().addQuoteToDefault(values);
        }
    }

    protected void onEachElement(Element e) {
        onEachElement(e, 0);
    }

    protected void afterParsing() {
    }

    protected Elements selectDate(Element e) {
        return e.select("span[class=date]");
    }

    protected Elements selectId(Element e) {
        return e.select("a[class=id]");
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

    protected void beforeParsing(Document document) {
        getDbHelper().clearDefault();
    }

    protected Document prepareRequest() throws IOException {
        HttpUriRequest httpRequest = getHttpRequest(getUrl());
        HttpResponse httpResponse = getHttpClient().execute(httpRequest);
        String encoding = getCharsetFromResponse(httpResponse);
        return Jsoup.parse(getInputStream(httpResponse), encoding, getUrl());
    }
}
