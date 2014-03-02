package ru.aim.anotheryetbashclient.helper.actions;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.aim.anotheryetbashclient.ActionsAndIntents;
import ru.aim.anotheryetbashclient.helper.BaseAction;
import ru.aim.anotheryetbashclient.helper.DbHelper;
import ru.aim.anotheryetbashclient.helper.f.Block;

import static java.lang.String.format;
import static ru.aim.anotheryetbashclient.helper.DbHelper.QUOTE_PUBLIC_ID;
import static ru.aim.anotheryetbashclient.helper.Utils.rethrowWithRuntime;
import static ru.aim.anotheryetbashclient.helper.actions.Package.getCharsetFromResponse;

public class BashRandomAction extends BaseAction {

    static final String URL = "http://bash.im/random";
    static final String MORE_URL = "http://bash.im%s";

    @Override
    public void apply() {
        rethrowWithRuntime(new Block() {
            @Override
            public void apply() throws Exception {
                final Intent intent = new Intent(ActionsAndIntents.REFRESH);
                String url;
                if (getIntent().hasExtra(ActionsAndIntents.NEXT_PAGE)) {
                    url = format(MORE_URL, getIntent().getStringExtra(ActionsAndIntents.NEXT_PAGE));
                } else {
                    url = URL;
                }
                HttpGet httpGet = new HttpGet(url);
                HttpResponse httpResponse = getHttpClient().execute(httpGet);
                String encoding = getCharsetFromResponse(httpResponse);
                Document document = Jsoup.parse(httpResponse.getEntity().getContent(), encoding, url);
                Elements quotesElements = document.select("div[class=quote]");
                for (Element e : quotesElements) {
                    Elements idElements = e.select("a[class=id]");
                    Elements dateElements = e.select("span[class=date]");
                    Elements textElements = e.select("div[class=text]");
                    if (getDbHelper().notExists(idElements.html())) {
                        ContentValues values = new ContentValues();
                        values.put(QUOTE_PUBLIC_ID, idElements.html());
                        values.put(DbHelper.QUOTE_DATE, dateElements.html());
                        values.put(DbHelper.QUOTE_IS_NEW, 1);
                        values.put(DbHelper.QUOTE_TEXT, textElements.html().trim());
                        getDbHelper().addNewQuote(values);
                    }
                }
                LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
                localBroadcastManager.sendBroadcast(intent);
            }
        });
    }
}
