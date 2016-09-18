package ru.aim.anotheryetbashclient.data.local

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import ru.aim.anotheryetbashclient.data.Quote

class QuoteSqlDao(private val tableName: String, override val db: SQLiteDatabase?) : QuoteDao {

    override fun saveAll(list: List<Quote>) {
        using(db) {
            inTransaction {
                list.forEach { doSave(this, it) }
            }
        }
    }

    override fun findById(id: Long): Quote? {
        var q: Quote? = null
        using(db) {
            q = db?.rawQuery(selectById, arrayOf(id.toString())).firstOrNull { matchFromCursor(this) }
        }
        return q
    }

    override fun findAll(): List<Quote> = db?.rawQuery(selectAll, null).mapWithClose { matchFromCursor(this) }

    private fun doSave(database: SQLiteDatabase?, q: Quote): Quote {
        var tmp: Quote? = null
        if (q.id != null) {
            tmp = findById(q.id ?: 0)
        }
        val cv = contentValues {
            put(QUOTE_PUBLIC_ID, q.publicId)
            put(QUOTE_DATE, q.date)
            put(QUOTE_IS_NEW, 1)
            put(QUOTE_TEXT, q.text)
            put(QUOTE_RATING, q.rating)
            put(QUOTE_FLAG, q.flag)
        }
        if (tmp == null) {
            q.id = database?.insert(tableName, null, cv)
        } else {
            database?.update(tableName, cv, "${QUOTE_ID} = ?", arrayOf(q.id.toString()))
        }
        return q
    }

    override fun save(q: Quote): Quote {
        return doSave(db, q)
    }

    private fun matchFromCursor(cursor: Cursor): Quote {
        val result = Quote(
                cursor.getLong(QUOTE_ID),
                cursor.getString(QUOTE_TEXT),
                cursor.getString(QUOTE_PUBLIC_ID),
                cursor.getString(QUOTE_RATING),
                cursor.getString(QUOTE_DATE),
                intToBoolean(cursor.getInt(QUOTE_IS_NEW)),
                cursor.getInt(QUOTE_FLAG)
        )
        return result
    }

    private fun intToBoolean(value: Int?) = when (value) {
        null -> null
        else -> value == 1
    }

    private val selectById = "select * from $tableName where ${QUOTE_ID} = ?"
    private val selectAll = "select * from $tableName"
}