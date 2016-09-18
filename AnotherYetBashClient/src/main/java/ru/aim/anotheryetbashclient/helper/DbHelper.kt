package ru.aim.anotheryetbashclient.helper

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ru.aim.anotheryetbashclient.QType
import java.util.*

class DbHelper(context: Context?) : SQLiteOpenHelper(context, DbHelper.DB_NAME, null, DbHelper.DB_VERSION) {

    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        for (s in TABLES) {
            createTable(sqLiteDatabase, s)
        }
    }

    internal fun createTable(sqLiteDatabase: SQLiteDatabase, tableName: String) {
        sqLiteDatabase.execSQL("CREATE TABLE $tableName ($QUOTE_ID INTEGER PRIMARY KEY AUTOINCREMENT, $QUOTE_PUBLIC_ID TEXT, $QUOTE_DATE TEXT, $QUOTE_IS_NEW INTEGER, $QUOTE_FLAG INTEGER,$QUOTE_TYPE INTEGER,$QUOTE_RATING TEXT, $QUOTE_PAGE INTEGER, $QUOTE_TEXT TEXT)")
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i2: Int) {
        for (s in TABLES) {
            sqLiteDatabase.execSQL("drop table if exists $s")
        }
        onCreate(sqLiteDatabase)
    }

    fun markRead(innerId: Long) {
        val db = writableDatabase!!
        db.execSQL("update $QUOTE_DEFAULT_TABLE set $QUOTE_IS_NEW = 0 where $QUOTE_ID = $innerId")
    }

    fun addToFavorite(publicId: String) {
        if (!isFavorite(publicId)) {
            val cursor = findQuotes(publicId)
            cursor.moveToFirst()
            val contentValues = ContentValues()
            contentValues.put(DbHelper.QUOTE_PUBLIC_ID, publicId)
            contentValues.put(DbHelper.QUOTE_RATING, cursor.getString(cursor.getColumnIndex(QUOTE_RATING)))
            contentValues.put(DbHelper.QUOTE_FLAG, cursor.getInt(cursor.getColumnIndex(QUOTE_FLAG)))
            contentValues.put(DbHelper.QUOTE_DATE, cursor.getString(cursor.getColumnIndex(QUOTE_DATE)))
            contentValues.put(DbHelper.QUOTE_IS_NEW, cursor.getInt(cursor.getColumnIndex(QUOTE_IS_NEW)))
            contentValues.put(DbHelper.QUOTE_TEXT, cursor.getString(cursor.getColumnIndex(QUOTE_TEXT)))
            val db = writableDatabase!!
            db.insert(QUOTE_FAVORITES_TABLE, null, contentValues)

        }
    }

    fun removeFromFavorite(publicId: String) {
        val db = writableDatabase
        if (isFavorite(publicId)) {
            db?.delete(QUOTE_FAVORITES_TABLE, QUOTE_PUBLIC_ID + " = ?", arrayOf(publicId))
        }

    }

    fun isFavorite(publicId: String): Boolean {
        val db = readableDatabase!!
        var cursor: Cursor? = null
        try {
            cursor = db.query(QUOTE_FAVORITES_TABLE, arrayOf(QUOTE_FLAG), QUOTE_PUBLIC_ID + " = ?",
                    arrayOf(publicId), null, null, null)
            return cursor!!.count > 0
        } finally {
            if (cursor != null) {
                cursor.close()
            }
        }
    }

    internal fun doFavorite(innerId: Long, value: Int) {
        val db = writableDatabase!!
        db.execSQL("update $QUOTE_DEFAULT_TABLE set $QUOTE_FLAG = $value where $QUOTE_ID = $innerId")
    }

    fun selectFromFavorites(): Cursor {
        return selectAll(QUOTE_FAVORITES_TABLE)
    }

    fun selectFromOffline(): Cursor {
        return selectAll(QUOTE_OFFLINE_TABLE)
    }

    val unread: Cursor
        get() {
            val database = readableDatabase!!
            val offlineMessagesLimit = Integer.toString(10)
            var cursor = database.rawQuery("select * from $QUOTE_DEFAULT_TABLE where $QUOTE_IS_NEW = ? limit $offlineMessagesLimit", arrayOf("1"))
            if (cursor.count < 10) {
                cursor.close()
                cursor = database.rawQuery("select * from $QUOTE_DEFAULT_TABLE where $QUOTE_IS_NEW = ? order by random() limit $offlineMessagesLimit", arrayOf("1"))
            }
            return cursor
        }

    fun exists(publicId: String): Boolean {
        val db = readableDatabase!!
        val cursor = db.rawQuery("select count(*) from $QUOTE_DEFAULT_TABLE where $QUOTE_PUBLIC_ID = ?", arrayOf(publicId))
        cursor.moveToFirst()
        val count = cursor.getLong(0)
        cursor.close()

        return count != 0L
    }

    fun notExists(id: String): Boolean {
        return !exists(id)
    }

    fun addQuoteToDefault(values: ContentValues): Long {
        return addQuote(QUOTE_DEFAULT_TABLE, values)
    }

    fun addQuoteToOffline(values: ContentValues): Long {
        return addQuote(QUOTE_OFFLINE_TABLE, values)
    }

    private fun getQuotesFromDefault(vararg quotes: String): Cursor {
        return getQuotes(QUOTE_DEFAULT_TABLE, *quotes)
    }

    private fun getQuotes(tableName: String, vararg quotes: String): Cursor {
        val db = readableDatabase!!
        val sql = "select * from " + tableName + " where " + QUOTE_PUBLIC_ID + " in (" +
                makePlaceholders(quotes.size) + ") order by " + QUOTE_PUBLIC_ID + " desc"
        return db.rawQuery(sql, quotes)
    }

    fun findQuotes(vararg quotes: String): Cursor {
        var cursor = getQuotesFromDefault(*quotes)
        if (cursor.count == 0) {
            cursor.close()
            cursor = getQuotes(QUOTE_OFFLINE_TABLE, *quotes)
        }
        return cursor
    }

    fun clearDefault() {
        clearTable(QUOTE_DEFAULT_TABLE)
    }

    fun clearTable(tableName: String) {
        val db = writableDatabase
        db.delete(tableName, null, null)
    }

    fun selectFromDefaultTable(): Cursor {
        return selectAll(QUOTE_DEFAULT_TABLE)
    }

    fun selectAll(tableName: String): Cursor {
        val db = readableDatabase!!
        return db.query(tableName, null, null, null, null, null, null)
    }

    fun selectFromOffline(page: Int): Cursor {
        val db = readableDatabase!!
        return db.query(QUOTE_OFFLINE_TABLE, null, QUOTE_FLAG + " = ?", arrayOf(Integer.toString(page)), null, null, null)
    }

    fun addQuote(tableName: String, values: ContentValues): Long {
        if (DEBUG) {
            L.d(TAG, "Add new quotes to $tableName values: $values")
        }
        val db = writableDatabase!!
        return db.insert(tableName, null, values)
    }

    fun updateQuote(contentValues: ContentValues) {
        val db = writableDatabase!!
        db.update(QUOTE_DEFAULT_TABLE, contentValues, QUOTE_PUBLIC_ID + " = ?", arrayOf(contentValues.getAsString(QUOTE_PUBLIC_ID)))
    }

    fun incrementRating(publicId: String) {
        updateRating(publicId, 1)
    }

    fun decrementRating(publicId: String) {
        updateRating(publicId, -1)
    }

    internal fun updateRating(publicId: String, to: Int) {
        val db = writableDatabase!!
        val cursor = findQuotes(publicId)
        cursor.moveToFirst()
        var value = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DbHelper.QUOTE_RATING)))
        value = value + to
        val values = ContentValues()
        values.put(DbHelper.QUOTE_RATING, value)
        db.update(QUOTE_DEFAULT_TABLE, values, QUOTE_PUBLIC_ID + " = ?", arrayOf(publicId))
    }

    fun isEmptyTable(tableName: String): Boolean {
        val db = readableDatabase
        var cursor: Cursor? = null
        var count: Long = 0

        cursor = db.rawQuery("select count(*) from " + tableName, null)
        cursor!!.moveToFirst()
        count = cursor.getLong(0)

        return count == 0L
    }

    val isEmptyFreshTable: Boolean
        get() = isEmptyTable(QUOTE_OFFLINE_TABLE)

    fun deleteOfflinePage(page: Int): Int {
        val db = writableDatabase
        return db.delete(QUOTE_OFFLINE_TABLE, QUOTE_FLAG + " = ?", arrayOf(Integer.toString(page)))
    }

    // dao
    fun save(type: QType, values: ContentValues) {
        if (exists(type, QUOTE_PUBLIC_ID)) {
            writableDatabase.update(type.tableName, values, QUOTE_PUBLIC_ID + " = ?", arrayOf(values.getAsString(QUOTE_PUBLIC_ID)))
        } else {
            writableDatabase.insert(type.tableName, null, values)
        }
    }

    fun findByPublicId(type: QType, publicId: String): Cursor {
        val db = readableDatabase
        return db.rawQuery("select * from " + type.tableName + " where " + QUOTE_PUBLIC_ID + " = ?", arrayOf(publicId))
    }

    fun exists(type: QType, publicId: String): Boolean {
        val cursor = findByPublicId(type, publicId)
        return cursor.count > 0

    }

    companion object {

        private val TAG = "DbHelper"
        private val DEBUG = true

        val DB_NAME = "quote_db"
        val DB_VERSION = 4

        /* TABLES */
        const val QUOTE_DEFAULT_TABLE = "quotes"
        const val QUOTE_FAVORITES_TABLE = "quotes_favorites"
        const val QUOTE_OFFLINE_TABLE = "quotes_fresh"
        const val QUOTE_MAIN_TABLE = "quotes_main"

        const val QUOTE_FRESH_TABLE = "quotes_fresh"
        const val QUOTE_RANDOM_TABLE = "quotes_random"
        const val QUOTE_BEST_TABLE = "quotes_best"
        const val QUOTE_RATING_TABLE = "quotes_rating"
        const val QUOTE_ABYSS_TABLE = "quotes_abyss"
        const val QUOTE_TOP_ABYSS_TABLE = "quotes_top_abyss"
        const val QUOTE_BEST_ABYSS_TABLE = "quotes_best_abyss"

        // default tables list
        var TABLES = Arrays.asList(
                QUOTE_DEFAULT_TABLE,
                QUOTE_FAVORITES_TABLE,
                QUOTE_FRESH_TABLE,
                QUOTE_RANDOM_TABLE,
                QUOTE_BEST_TABLE,
                QUOTE_RATING_TABLE,
                QUOTE_ABYSS_TABLE,
                QUOTE_TOP_ABYSS_TABLE,
                QUOTE_BEST_ABYSS_TABLE)

        /* FIELDS */
        const val QUOTE_ID = "_id"
        const val QUOTE_PUBLIC_ID = "quote_public_id"
        const val QUOTE_DATE = "quote_date"
        const val QUOTE_IS_NEW = "quote_is_new"
        const val QUOTE_FLAG = "quote_flag"
        const val QUOTE_TEXT = "quote_text"
        const val QUOTE_RATING = "quote_rating"
        const val QUOTE_TYPE = "quote_type"
        const val QUOTE_PAGE = "quote_page"

        @Volatile private var dbHelper: DbHelper? = null

        fun getInstance(context: Context?): DbHelper {
            if (dbHelper == null) {
                synchronized (DbHelper::class.java) {
                    if (dbHelper == null) {
                        dbHelper = DbHelper(context?.applicationContext)
                    }
                }
            }
            return dbHelper!!
        }

        val instance: DbHelper
            get() = getInstance(null)

        internal fun makePlaceholders(len: Int): String {
            if (len < 1) {
                // It will lead to an invalid query anyway ..
                throw RuntimeException("No placeholders")
            } else {
                val sb = StringBuilder(len * 2 - 1)
                sb.append("?")
                for (i in 1..len - 1) {
                    sb.append(",?")
                }
                return sb.toString()
            }
        }
    }
}
