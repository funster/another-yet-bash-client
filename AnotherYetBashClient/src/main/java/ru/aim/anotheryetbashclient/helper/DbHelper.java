package ru.aim.anotheryetbashclient.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

@SuppressWarnings("unused")
public class DbHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "quote_db";
    public static final String QUOTE_TABLE = "quotes";
    public static final String QUOTE_ABYSS_TABLE = "quotes_abyss";
    public static final String QUOTE_ID = "_id";
    public static final String QUOTE_PUBLIC_ID = "quote_public_id";
    public static final String QUOTE_DATE = "quote_date";
    public static final String QUOTE_IS_NEW = "quote_is_new";
    public static final String QUOTE_FLAG = "quote_flag";
    public static final String QUOTE_TEXT = "quote_text";
    public static final String QUOTE_RATING = "quote_rating";

    public DbHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    static String makePlaceholders(int len) {
        if (len < 1) {
            // It will lead to an invalid query anyway ..
            throw new RuntimeException("No placeholders");
        } else {
            StringBuilder sb = new StringBuilder(len * 2 - 1);
            sb.append("?");
            for (int i = 1; i < len; i++) {
                sb.append(",?");
            }
            return sb.toString();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + QUOTE_TABLE +
                " (" + QUOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QUOTE_PUBLIC_ID + " TEXT, " + QUOTE_DATE + " TEXT, " +
                QUOTE_IS_NEW + " INTEGER, " + QUOTE_FLAG + " INTEGER, " +
                QUOTE_RATING + " TEXT, " + QUOTE_TEXT + " TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE " + QUOTE_ABYSS_TABLE +
                " (" + QUOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QUOTE_PUBLIC_ID + " TEXT, " + QUOTE_DATE + " TEXT, " +
                QUOTE_IS_NEW + " INTEGER, " + QUOTE_FLAG + " INTEGER," +
                QUOTE_RATING + " TEXT, " + QUOTE_TEXT + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
    }

    public void markRead(long innerId) {
        SQLiteDatabase db = getWritableDatabase();
        assert db != null;
        db.execSQL("update " + QUOTE_TABLE + " set " + QUOTE_IS_NEW + " = 0 where " + QUOTE_ID + " = " + innerId);
        db.close();
    }

    public void addToFavorite(long innerId) {
        doFavorite(innerId, 1);
    }

    public void removeFromFavorite(long innerId) {
        doFavorite(innerId, 0);
    }

    public boolean isFavorite(long innerId) {
        SQLiteDatabase db = getReadableDatabase();
        assert db != null;
        Cursor cursor = db.query(QUOTE_TABLE, new String[]{QUOTE_FLAG}, QUOTE_ID + " = ?",
                new String[]{Long.toString(innerId)}, null, null, null);
        if (cursor.getCount() == 0) {
            throw new IllegalArgumentException("Can't find quote with inner id: " + innerId);
        }
        cursor.moveToFirst();
        int value = cursor.getInt(cursor.getColumnIndex(QUOTE_FLAG));
        cursor.close();
        db.close();
        return value == 1;
    }

    void doFavorite(long innerId, int value) {
        SQLiteDatabase db = getWritableDatabase();
        assert db != null;
        db.execSQL("update " + QUOTE_TABLE + " set " + QUOTE_FLAG + " = " + value + " where " + QUOTE_ID + " = " + innerId);
        db.close();
    }

    public Cursor getFavorites() {
        SQLiteDatabase db = getReadableDatabase();
        assert db != null;
        return db.rawQuery("select " + QUOTE_PUBLIC_ID + "  from " + QUOTE_TABLE + " where " + QUOTE_FLAG + " = 1", null);
    }

    public Cursor getUnread() {
        SQLiteDatabase database = getReadableDatabase();
        assert database != null;
        String offlineMessagesLimit = Integer.toString(10);
        Cursor cursor = database.rawQuery("select * from " + QUOTE_TABLE + " where " + QUOTE_IS_NEW + " = ? " +
                "limit " + offlineMessagesLimit, new String[]{"1"});
        if (cursor.getCount() < 10) {
            cursor.close();
            cursor = database.rawQuery("select * from " + QUOTE_TABLE + " where " + QUOTE_IS_NEW + " = ? " +
                    "order by random() limit " + offlineMessagesLimit, new String[]{"1"});
        }
        return cursor;
    }

    public boolean exists(String publicId) {
        SQLiteDatabase db = getReadableDatabase();
        assert db != null;
        final Cursor cursor = db.rawQuery("select count(*) from " + QUOTE_TABLE + " where " +
                QUOTE_PUBLIC_ID + " = ?", new String[]{publicId});
        cursor.moveToFirst();
        long count = cursor.getLong(0);
        cursor.close();
        db.close();
        return count != 0;
    }

    public boolean notExists(String id) {
        return !exists(id);
    }

    public long addNewQuote(ContentValues values) {
        SQLiteDatabase db = getWritableDatabase();
        assert db != null;
        long id = db.insert(QUOTE_TABLE, null, values);
        db.close();
        return id;
    }

    public Cursor getQuotes(String... quotes) {
        SQLiteDatabase db = getReadableDatabase();
        assert db != null;
        String sql = "select * from " + QUOTE_TABLE + " where " + QUOTE_PUBLIC_ID + " in (" +
                makePlaceholders(quotes.length) + ") order by " + QUOTE_PUBLIC_ID + " desc";
        return db.rawQuery(sql, quotes);
    }

    public void clearAbyss() {
        SQLiteDatabase db = getWritableDatabase();
        assert db != null;
        db.delete(QUOTE_ABYSS_TABLE, null, null);
        db.close();
    }

    public void clearDefault() {
        SQLiteDatabase db = getWritableDatabase();
        assert db != null;
        db.delete(QUOTE_TABLE, null, null);
        db.close();
    }

    public Cursor getDefault() {
        SQLiteDatabase db = getReadableDatabase();
        assert db != null;
        return db.query(QUOTE_TABLE, null, null, null, null, null, null);
    }

    public Cursor getAbyss() {
        SQLiteDatabase db = getReadableDatabase();
        assert db != null;
        return db.query(QUOTE_ABYSS_TABLE, null, null, null, null, null, null);
    }

    public void addNewQuoteAbyss(ContentValues values) {
        SQLiteDatabase db = getWritableDatabase();
        assert db != null;
        db.insert(QUOTE_ABYSS_TABLE, null, values);
        db.close();
    }

    public void updateQuote(ContentValues contentValues) {
        SQLiteDatabase db = getWritableDatabase();
        assert db != null;
        db.update(QUOTE_TABLE, contentValues, QUOTE_PUBLIC_ID + " = ?", new String[]{contentValues.getAsString(QUOTE_PUBLIC_ID)});
        db.close();
    }
}
