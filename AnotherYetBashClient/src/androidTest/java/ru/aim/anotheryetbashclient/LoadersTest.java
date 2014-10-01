package ru.aim.anotheryetbashclient;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import ru.aim.anotheryetbashclient.helper.DbHelper;
import ru.aim.anotheryetbashclient.helper.f.Function1;
import ru.aim.anotheryetbashclient.loaders.BestLoader;

public class LoadersTest extends ActivityInstrumentationTestCase2<MainActivity> {

    MainActivity mainActivity;
    DbHelper dbHelper;

    public LoadersTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mainActivity = getActivity();
        dbHelper = new DbHelper(mainActivity);
    }

    @MediumTest
    public void testBestLoader() throws Exception {
        BestLoader bestLoader = new BestLoader(mainActivity, Bundle.EMPTY) {
            @Override
            protected Document prepareRequest() throws IOException {
                return Jsoup.parse(getInstrumentation().getContext().getAssets().
                        open("best_sample.html"), "windows-1251", BestLoader.URL);
            }
        };
        bestLoader.setDbHelper(dbHelper);
        Cursor cursor = bestLoader.doInBackground();
        assertNotNull(cursor);
        assertSame(cursor.getCount(), 35);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        query(db, "select count(*) from " + DbHelper.QUOTE_DEFAULT_TABLE + " where " + DbHelper.QUOTE_FLAG + " = ?",
                new String[]{"0"}, new Function1<Cursor, Void>() {
                    @Override
                    public Void apply(Cursor arg0) {
                        arg0.moveToFirst();
                        assertEquals(arg0.getLong(0), 10);
                        return null;
                    }
                });

        query(db, "select count(*) from " + DbHelper.QUOTE_DEFAULT_TABLE + " where " + DbHelper.QUOTE_FLAG + " = ?",
                new String[]{"1"}, new Function1<Cursor, Void>() {
                    @Override
                    public Void apply(Cursor arg0) {
                        arg0.moveToFirst();
                        assertEquals(arg0.getLong(0), 25);
                        return null;
                    }
                });
    }

    static void query(SQLiteDatabase db, String query, String[] args, Function1<Cursor, Void> f) {
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, args);
            f.apply(cursor);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}