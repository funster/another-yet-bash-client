package ru.aim.anotheryetbashclient.tasks;

import android.content.ContentValues;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ru.aim.anotheryetbashclient.async.OkHttpSpiceRequest;
import ru.aim.anotheryetbashclient.helper.DbHelper;

import static ru.aim.anotheryetbashclient.helper.DbHelper.QUOTE_PUBLIC_ID;

public abstract class AbstractOkHttpRequest extends OkHttpSpiceRequest<String> {

    public AbstractOkHttpRequest() {
        super(String.class);
    }

    @Override
    public String loadDataFromNetwork() throws Exception {
        Response response = execRequest();
        Document document = Jsoup.parse(response.body().byteStream(), "windows-1251", getUrl());
        beforeParsing(document);
        parse(document);
        afterParsing();
        return "";
    }

    private Response execRequest() throws Exception {
        Request request = new Request.Builder().url(getUrl()).build();
        return getOkHttpClient().newCall(request).execute();
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

    abstract void afterParsing();

    abstract String getUrl();

    void beforeParsing(Document document) {
    }

    void parse(Document document) {
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
    }

    Elements selectDate(Element e) {
        return e.select("span[class=date]");
    }

    Elements selectId(Element e) {
        return e.select("a[class=id]");
    }

    protected void saveQuote(ContentValues values) {
    }
}
