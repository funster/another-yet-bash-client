package ru.aim.anotheryetbashclient.loaders;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ru.aim.anotheryetbashclient.ActionsAndIntents;
import ru.aim.anotheryetbashclient.BashApplication;
import ru.aim.anotheryetbashclient.helper.DbHelper;
import ru.aim.anotheryetbashclient.helper.L;

import static ru.aim.anotheryetbashclient.helper.DbHelper.QUOTE_PUBLIC_ID;
import static ru.aim.anotheryetbashclient.loaders.Package.getCharsetFromResponse;
import static ru.aim.anotheryetbashclient.loaders.Package.wrapWithRoot;

/**
 *
 */
public class FreshLoader extends AbstractLoader<FreshResult> {

    static final String ROOT_PAGE = wrapWithRoot("");
    static final String NEXT_PAGE = wrapWithRoot("index/%s");

    public static final int ID = ActionsAndIntents.TYPE_NEW;

    int mCurrentPage;

    public FreshLoader(Context context, Bundle bundle) {
        super(context);
        mCurrentPage = bundle.getInt(ActionsAndIntents.CURRENT_PAGE, 0);
    }

    @Override
    public FreshResult doInBackground() throws Exception {
        FreshResult result = new FreshResult();
        String uri;
        if (mCurrentPage == 0) {
            uri = ROOT_PAGE;
        } else {
            uri = String.format(NEXT_PAGE, mCurrentPage);
        }

        HttpGet httpRequest = new HttpGet(uri);
        BashApplication app = (BashApplication) getContext().getApplicationContext();
        HttpResponse httpResponse = app.getHttpClient().execute(httpRequest);
        Document document = Jsoup.parse(httpResponse.getEntity().getContent(), getCharsetFromResponse(httpResponse), uri);
        if (mCurrentPage != -1) {
            Elements elements = document.select("input[class=page]");
            String page = null;
            for (Element e : elements) {
                page = e.attr("value");
            }
            if (!TextUtils.isEmpty(page)) {
                result.currentPage = Integer.parseInt(page);
                result.maxPage = result.currentPage;
            }
        }
        Elements quotesElements = document.select("div[class=quote]");
        L.d(TAG, "Quotes size " + quotesElements.size());
        getDbHelper().clearDefault();
        for (Element e : quotesElements) {
            Elements idElements = e.select("a[class=id]");
            Elements dateElements = e.select("span[class=date]");
            Elements textElements = e.select("div[class=text]");
            Elements ratingElements = e.select("span[class=rating]");
            if (!textElements.isEmpty()) {
                String id = idElements.html();
                if (getDbHelper().notExists(id)) {
                    ContentValues values = new ContentValues();
                    values.put(QUOTE_PUBLIC_ID, idElements.html());
                    values.put(DbHelper.QUOTE_DATE, dateElements.html());
                    values.put(DbHelper.QUOTE_IS_NEW, 1);
                    values.put(DbHelper.QUOTE_RATING, ratingElements.html().trim());
                    values.put(DbHelper.QUOTE_TEXT, textElements.html().trim());
                    L.d(TAG, "Insert new item: " + values);
                    getDbHelper().addNewQuote(values);
                }
            }
        }
        result.cursor = getDbHelper().getDefault();
        return result;
    }

    protected String getUrl() {
        if (mCurrentPage == 0) {
            return ROOT_PAGE;
        } else {
            return String.format(NEXT_PAGE, mCurrentPage);
        }
    }
}
