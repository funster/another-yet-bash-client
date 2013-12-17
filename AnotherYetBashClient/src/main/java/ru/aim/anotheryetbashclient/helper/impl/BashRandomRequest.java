package ru.aim.anotheryetbashclient.helper.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.http.AndroidHttpClient;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import ru.aim.anotheryetbashclient.helper.DbHelper;
import ru.aim.anotheryetbashclient.helper.QuiteRequest;

import static ru.aim.anotheryetbashclient.helper.DbHelper.QUOTE_PUBLIC_ID;
import static ru.aim.anotheryetbashclient.helper.DbHelper.QUOTE_TABLE;

public class BashRandomRequest implements QuiteRequest {

    static final String URL = "http://bash.im/random";

    @Override
    public void doRequest(SQLiteDatabase database, AndroidHttpClient httpClient) throws IOException {
        HttpGet httpRequest = new HttpGet(URL);
        HttpResponse httpResponse = httpClient.execute(httpRequest);
        Document document = Jsoup.parse(httpResponse.getEntity().getContent(), WINDOWS_1215, URL);
        Elements quotesElements = document.select("div[class=quote]");
        for (Element e : quotesElements) {
            Elements idElements = e.select("a[class=id]");
            Elements dateElements = e.select("span[class=date]");
            Elements textElements = e.select("div[class=text]");
            if (!textElements.isEmpty()) {
                String id = idElements.html();
                Cursor cursor = database.rawQuery("select count(*) from " + QUOTE_TABLE + " where " +
                        QUOTE_PUBLIC_ID + " = ?", new String[]{id});
                cursor.moveToFirst();
                long count = cursor.getLong(0);
                if (count == 0) {
                    ContentValues values = new ContentValues();
                    values.put(QUOTE_PUBLIC_ID, idElements.html());
                    values.put(DbHelper.QUOTE_DATE, dateElements.html());
                    values.put(DbHelper.QUOTE_IS_NEW, 1);
                    values.put(DbHelper.QUOTE_TEXT, textElements.html().trim());
                    database.insert(QUOTE_TABLE, null, values);
                }
                cursor.close();
            }
        }
    }
}
