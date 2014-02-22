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
import ru.aim.anotheryetbashclient.ActionsAndIntents;
import ru.aim.anotheryetbashclient.helper.BaseAction;
import ru.aim.anotheryetbashclient.helper.DbHelper;
import ru.aim.anotheryetbashclient.helper.Utils;
import ru.aim.anotheryetbashclient.helper.f.Block;

import java.util.ArrayList;

import static ru.aim.anotheryetbashclient.helper.DbHelper.QUOTE_PUBLIC_ID;
import static ru.aim.anotheryetbashclient.helper.Utils.WINDOWS_1215;
import static ru.aim.anotheryetbashclient.helper.Utils.rethrowWithRuntime;

/**
 *
 */
public class BashByRating extends BaseAction {

    static final String ROOT_PAGE = "http://bash.im/byrating";
    static final String NEXT_PAGE = "http://bash.im/byrating/%s";

    @Override
    public void apply() {
        rethrowWithRuntime(new Block() {
            @Override
            public void apply() throws Exception {
                LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
                Intent intent = new Intent(ActionsAndIntents.REFRESH);
                int currentPage = 0;
                String uri;
                if (getIntent().hasExtra(ActionsAndIntents.CURRENT_PAGE)) {
                    currentPage = getIntent().getIntExtra(ActionsAndIntents.CURRENT_PAGE, 0);
                    uri = String.format(NEXT_PAGE, currentPage - 1);
                } else {
                    uri = ROOT_PAGE;
                }
                HttpGet httpRequest = new HttpGet(uri);
                HttpResponse httpResponse = getHttpClient().execute(httpRequest);
                Document document = Jsoup.parse(httpResponse.getEntity().getContent(), WINDOWS_1215, uri);
                if (!getIntent().hasExtra(ActionsAndIntents.CURRENT_PAGE)) {
                    Elements elements = document.select("input[class=page]");
                    String page = null;
                    for (Element e : elements) {
                        page = e.attr("value");
                    }
                    if (!TextUtils.isEmpty(page)) {
                        assert page != null;
                        currentPage = Integer.parseInt(page);
                    }
                } else {
                    currentPage += 1;
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
                intent.putStringArrayListExtra(ActionsAndIntents.IDS, list);
                intent.putExtra(ActionsAndIntents.CURRENT_PAGE, currentPage);
                localBroadcastManager.sendBroadcast(intent);
                Utils.sendMessageIntent(getContext(), "Current page: " + currentPage);
            }
        });
    }
}
