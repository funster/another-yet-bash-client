package ru.aim.anotheryetbashclient.helper.actions;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Calendar;

import ru.aim.anotheryetbashclient.SettingsHelper;
import ru.aim.anotheryetbashclient.helper.DbHelper;
import ru.aim.anotheryetbashclient.helper.L;

import static ru.aim.anotheryetbashclient.helper.DbHelper.QUOTE_PUBLIC_ID;
import static ru.aim.anotheryetbashclient.loaders.FreshLoader.NEXT_PAGE;
import static ru.aim.anotheryetbashclient.loaders.FreshLoader.ROOT_PAGE;
import static ru.aim.anotheryetbashclient.loaders.Package.getCharsetFromResponse;
import static ru.aim.anotheryetbashclient.loaders.QuoteLoader.getInputStream;

public class OfflineDownloaderAction extends AbstractAction {

    private static final String TAG = "OfflineDownloaderAction";
    public static final String UPDATE_COMPLETED = "ru.aim.anotheryetbashclient.helper.actions.UPDATE_COMPLETED";

    @Override
    public void execute() throws Exception {
        int pages = SettingsHelper.getOfflinePages(getContext());
        int maxPage = -1;
        getDbHelper().clearOffline();
        for (int i = 0; i < pages; i++) {
            String url;
            if (i == 0) {
                url = ROOT_PAGE;
            } else {
                url = String.format(NEXT_PAGE, maxPage - i);
            }
            HttpGet httpRequest = buildGetRequest(url);
            L.d(TAG, "Requesting page: " + url);
            HttpResponse httpResponse = getHttpClient().execute(httpRequest);
            Document document = Jsoup.parse(getInputStream(httpResponse), getCharsetFromResponse(httpResponse), url);
            if (i == 0) {
                Elements elements = document.select("input[class=page]");
                String page = null;
                for (Element e : elements) {
                    page = e.attr("value");
                }
                if (!TextUtils.isEmpty(page)) {
                    maxPage = Integer.parseInt(page);
                }
            }
            Elements quotesElements = document.select("div[class=quote]");
            for (Element e : quotesElements) {
                Elements idElements = e.select("a[class=id]");
                Elements dateElements = e.select("span[class=date]");
                Elements textElements = e.select("div[class=text]");
                Elements ratingElements = e.select("span[class=rating]");
                if (!textElements.isEmpty()) {
                    String id = idElements.html();
                    ContentValues values = new ContentValues();
                    values.put(QUOTE_PUBLIC_ID, idElements.html());
                    values.put(DbHelper.QUOTE_DATE, dateElements.html());
                    values.put(DbHelper.QUOTE_IS_NEW, 1);
                    values.put(DbHelper.QUOTE_RATING, ratingElements.html().trim());
                    values.put(DbHelper.QUOTE_TEXT, textElements.html().trim());
                    values.put(DbHelper.QUOTE_FLAG, i);
                    getDbHelper().addQuoteToOffline(values);
                }
            }
        }
        SettingsHelper.writeTimestamp(getContext(), Calendar.getInstance().getTimeInMillis());
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent(UPDATE_COMPLETED));
    }
}
