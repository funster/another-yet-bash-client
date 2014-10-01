package ru.aim.anotheryetbashclient.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

@SuppressWarnings("unused")
public class DbHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "quote_db";
    public static final String QUOTE_DEFAULT_TABLE = "quotes";
    public static final String QUOTE_ABYSS_TABLE = "quotes_abyss";
    public static final String QUOTE_FAVORITES_TABLE = "quotes_favorites";
    public static final String QUOTE_FRESH_TABLE = "quotes_fresh";
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
        createTable(sqLiteDatabase, QUOTE_DEFAULT_TABLE);
        createTable(sqLiteDatabase, QUOTE_FAVORITES_TABLE);
        createTable(sqLiteDatabase, QUOTE_FRESH_TABLE);
    }

    void createTable(SQLiteDatabase sqLiteDatabase, String tableName) {
        sqLiteDatabase.execSQL("CREATE TABLE " + tableName +
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
        db.execSQL("update " + QUOTE_DEFAULT_TABLE + " set " + QUOTE_IS_NEW + " = 0 where " + QUOTE_ID + " = " + innerId);
        db.close();
    }

    public void addToFavorite(String publicId) {
        if (!isFavorite(publicId)) {
            Cursor cursor = getQuotes(publicId);
            cursor.moveToFirst();
            ContentValues contentValues = new ContentValues();
            contentValues.put(DbHelper.QUOTE_PUBLIC_ID, publicId);
            contentValues.put(DbHelper.QUOTE_RATING, cursor.getString(cursor.getColumnIndex(QUOTE_RATING)));
            contentValues.put(DbHelper.QUOTE_FLAG, cursor.getInt(cursor.getColumnIndex(QUOTE_FLAG)));
            contentValues.put(DbHelper.QUOTE_DATE, cursor.getString(cursor.getColumnIndex(QUOTE_DATE)));
            contentValues.put(DbHelper.QUOTE_IS_NEW, cursor.getInt(cursor.getColumnIndex(QUOTE_IS_NEW)));
            contentValues.put(DbHelper.QUOTE_TEXT, cursor.getString(cursor.getColumnIndex(QUOTE_TEXT)));
            SQLiteDatabase db = getWritableDatabase();
            assert db != null;
            db.insert(QUOTE_FAVORITES_TABLE, null, contentValues);
            db.close();
        }
    }

    public void removeFromFavorite(String publicId) {
        SQLiteDatabase db = getWritableDatabase();
        assert db != null;
        if (isFavorite(publicId)) {
            db.delete(QUOTE_FAVORITES_TABLE, QUOTE_PUBLIC_ID + " = ?", new String[]{publicId});
        }
        db.close();
    }

    public boolean isFavorite(String publicId) {
        SQLiteDatabase db = getReadableDatabase();
        assert db != null;
        Cursor cursor = null;
        try {
            cursor = db.query(QUOTE_FAVORITES_TABLE, new String[]{QUOTE_FLAG}, QUOTE_PUBLIC_ID + " = ?",
                    new String[]{publicId}, null, null, null);
            return cursor.getCount() > 0;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    void doFavorite(long innerId, int value) {
        SQLiteDatabase db = getWritableDatabase();
        assert db != null;
        db.execSQL("update " + QUOTE_DEFAULT_TABLE + " set " + QUOTE_FLAG + " = " + value + " where " + QUOTE_ID + " = " + innerId);
        db.close();
    }

    public Cursor selectFromFavorites() {
        return selectAll(QUOTE_FAVORITES_TABLE);
    }

    public Cursor getUnread() {
        SQLiteDatabase database = getReadableDatabase();
        assert database != null;
        String offlineMessagesLimit = Integer.toString(10);
        Cursor cursor = database.rawQuery("select * from " + QUOTE_DEFAULT_TABLE + " where " + QUOTE_IS_NEW + " = ? " +
                "limit " + offlineMessagesLimit, new String[]{"1"});
        if (cursor.getCount() < 10) {
            cursor.close();
            cursor = database.rawQuery("select * from " + QUOTE_DEFAULT_TABLE + " where " + QUOTE_IS_NEW + " = ? " +
                    "order by random() limit " + offlineMessagesLimit, new String[]{"1"});
        }
        return cursor;
    }

    public boolean exists(String publicId) {
        SQLiteDatabase db = getReadableDatabase();
        assert db != null;
        final Cursor cursor = db.rawQuery("select count(*) from " + QUOTE_DEFAULT_TABLE + " where " +
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

    public long addQuoteToDefault(ContentValues values) {
        return addQuote(QUOTE_DEFAULT_TABLE, values);
    }

    public long addQuoteToFresh(ContentValues values) {
        return addQuote(QUOTE_FRESH_TABLE, values);
    }

    public Cursor getQuotes(String... quotes) {
        SQLiteDatabase db = getReadableDatabase();
        assert db != null;
        String sql = "select * from " + QUOTE_DEFAULT_TABLE + " where " + QUOTE_PUBLIC_ID + " in (" +
                makePlaceholders(quotes.length) + ") order by " + QUOTE_PUBLIC_ID + " desc";
        return db.rawQuery(sql, quotes);
    }

    public void clearAbyss() {
        clearTable(QUOTE_ABYSS_TABLE);
    }

    public void clearDefault() {
        clearTable(QUOTE_DEFAULT_TABLE);
    }

    public void clearTable(String tableName) {
        SQLiteDatabase db = getWritableDatabase();
        assert db != null;
        db.delete(tableName, null, null);
        db.close();
    }

    public Cursor selectFromDefaultTable() {
        return selectAll(QUOTE_DEFAULT_TABLE);
    }

    public Cursor selectAll(String tableName) {
        SQLiteDatabase db = getReadableDatabase();
        assert db != null;
        return db.query(tableName, null, null, null, null, null, null);
    }

    public Cursor selectFromAbyss() {
        return selectAll(QUOTE_ABYSS_TABLE);
    }

    public void addQuoteToAbyss(ContentValues values) {
        addQuote(QUOTE_ABYSS_TABLE, values);
    }

    public long addQuote(String tableName, ContentValues values) {
        SQLiteDatabase db = getWritableDatabase();
        assert db != null;
        long id = db.insert(tableName, null, values);
        db.close();
        return id;
    }

    public void updateQuote(ContentValues contentValues) {
        SQLiteDatabase db = getWritableDatabase();
        assert db != null;
        db.update(QUOTE_DEFAULT_TABLE, contentValues, QUOTE_PUBLIC_ID + " = ?", new String[]{contentValues.getAsString(QUOTE_PUBLIC_ID)});
        db.close();
    }

    public void incrementRating(String publicId) {
        updateRating(publicId, 1);
    }

    public void decrementRating(String publicId) {
        updateRating(publicId, -1);
    }

    void updateRating(String publicId, int to) {
        SQLiteDatabase db = getWritableDatabase();
        assert db != null;
        Cursor cursor = getQuotes(publicId);
        cursor.moveToFirst();
        int value = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DbHelper.QUOTE_RATING)));
        value = value + to;
        ContentValues values = new ContentValues();
        values.put(DbHelper.QUOTE_RATING, value);
        db.update(QUOTE_DEFAULT_TABLE, values, QUOTE_PUBLIC_ID + " = ?", new String[]{publicId});
        db.close();
    }

    public boolean isEmptyTable(String tableName) {
        SQLiteDatabase db = getReadableDatabase();
        assert db != null;
        final Cursor cursor = db.rawQuery("select count(*) from " + tableName, null);
        cursor.moveToFirst();
        long count = cursor.getLong(0);
        cursor.close();
        db.close();
        return count == 0;
    }

    public boolean isEmptyFreshTable() {
        return isEmptyTable(QUOTE_FRESH_TABLE);
    }

    public void clearFresh() {
        clearTable(QUOTE_FRESH_TABLE);
    }
}
