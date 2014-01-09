package ru.aim.anotheryetbashclient.helper.impl;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
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

import static ru.aim.anotheryetbashclient.helper.DbHelper.QUOTE_PUBLIC_ID;
import static ru.aim.anotheryetbashclient.helper.DbHelper.QUOTE_TABLE;
import static ru.aim.anotheryetbashclient.helper.Utils.UTF_8;
import static ru.aim.anotheryetbashclient.helper.Utils.rethrowWithRuntime;

@SuppressWarnings("unused")
public class BashBestAction extends BaseAction {

    public static final String TAG = "BashBestAction";

    static final String URL = "http://bash.im/best";

    @Override
    public void apply() {
        rethrowWithRuntime(new Block() {
            @Override
            public void apply() throws Exception {
                HttpGet httpRequest = new HttpGet(URL);
                HttpResponse httpResponse = httpClient.execute(httpRequest);
                Document document = Jsoup.parse(httpResponse.getEntity().getContent(), UTF_8, URL);
                Elements quotesElements = document.select("div[class=quote]");
                for (Element e : quotesElements) {
                    Elements idElements = e.select("a[class=id]");
                    Elements dateElements = e.select("span[class=date]");
                    Elements textElements = e.select("div[class=text]");
                    if (!textElements.isEmpty()) {
                        String id = idElements.html();
                        Cursor cursor = db.rawQuery("select count(*) from " + QUOTE_TABLE + " where " +
                                QUOTE_PUBLIC_ID + " = ?", new String[]{id});
                        cursor.moveToFirst();
                        long count = cursor.getLong(0);
                        if (count == 0) {
                            ContentValues values = new ContentValues();
                            values.put(QUOTE_PUBLIC_ID, idElements.html());
                            values.put(DbHelper.QUOTE_DATE, dateElements.html());
                            values.put(DbHelper.QUOTE_IS_NEW, 1);
                            values.put(DbHelper.QUOTE_TEXT, textElements.html().trim());
                            db.insert(QUOTE_TABLE, null, values);
                        }
                        cursor.close();
                    }
                }
            }
        });
    }
}
