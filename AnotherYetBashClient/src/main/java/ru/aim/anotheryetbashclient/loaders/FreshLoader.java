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
import ru.aim.anotheryetbashclient.helper.DbHelper;

import java.util.ArrayList;

import static ru.aim.anotheryetbashclient.helper.DbHelper.QUOTE_PUBLIC_ID;
import static ru.aim.anotheryetbashclient.helper.actions.Package.getCharsetFromResponse;
import static ru.aim.anotheryetbashclient.helper.actions.Package.wrapWithRoot;
import static ru.aim.anotheryetbashclient.loaders.Utils.getHttpClient;

/**
 *
 */
public class FreshLoader extends AbstractLoader<FreshResult> {

    static final String ROOT_PAGE = wrapWithRoot("");
    static final String NEXT_PAGE = wrapWithRoot("index/%s");


    public static final int ID = 1;
    public static final String CURRENT_PAGE = "current-page";

    int mCurrentPage;

    public FreshLoader(Context context, Bundle bundle) {
        super(context);
        mCurrentPage = bundle.getInt(CURRENT_PAGE, -1);
    }

    @Override
    public FreshResult doInBackground() throws Exception {
        FreshResult freshResult = new FreshResult();
        String uri;
        if (mCurrentPage == -1) {
            uri = ROOT_PAGE;
        } else {
            uri = String.format(NEXT_PAGE, mCurrentPage);
        }

        HttpGet httpRequest = new HttpGet(uri);
        HttpResponse httpResponse = getHttpClient().execute(httpRequest);
        Document document = Jsoup.parse(httpResponse.getEntity().getContent(), getCharsetFromResponse(httpResponse), uri);
        if (mCurrentPage != -1) {
            Elements elements = document.select("input[class=page]");
            String page = null;
            for (Element e : elements) {
                page = e.attr("value");
            }
            if (!TextUtils.isEmpty(page)) {
                assert page != null;
                freshResult.currentPage = Integer.parseInt(page);
                freshResult.maxPage = mCurrentPage;
            }
        }
        Elements quotesElements = document.select("div[class=quote]");
        ArrayList<String> list = new ArrayList<String>();
        for (Element e : quotesElements) {
            Elements idElements = e.select("a[class=id]");
            Elements dateElements = e.select("span[class=date]");
            Elements textElements = e.select("div[class=text]");
            if (!textElements.isEmpty()) {
                String id = idElements.html();
                list.add(id);
                if (getDbHelper().notExists(id)) {
                    ContentValues values = new ContentValues();
                    values.put(QUOTE_PUBLIC_ID, idElements.html());
                    values.put(DbHelper.QUOTE_DATE, dateElements.html());
                    values.put(DbHelper.QUOTE_IS_NEW, 1);
                    values.put(DbHelper.QUOTE_TEXT, textElements.html().trim());
                    getDbHelper().addNewQuote(values);
                }
            }
        }
        return null;
    }
}
