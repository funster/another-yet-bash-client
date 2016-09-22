package ru.aim.anotheryetbashclient.data.source.local.mapper

import android.content.ContentValues
import android.database.Cursor
import ru.aim.anotheryetbashclient.data.Quote
import ru.aim.anotheryetbashclient.data.source.local.*

interface CursorMapper<T> where T : Quote {

    fun fromCursor(cursor: Cursor): T

    fun toContentValues(q: T): ContentValues
}

abstract class AbstractMapper<T> : CursorMapper<T> where T : Quote {

    override fun fromCursor(cursor: Cursor): T {
        val q = newInstance()
        q.id = cursor.getLong(_ID)
        q.text = cursor.getString(QUOTE_TEXT)
        q.publicId = cursor.getString(QUOTE_PUBLIC_ID)
        q.rating = cursor.getString(QUOTE_RATING)
        q.date = cursor.getString(QUOTE_DATE)
        q.isNew = intToBoolean(cursor.getInt(QUOTE_IS_NEW))
        q.flag = cursor.getInt(QUOTE_FLAG)
        q.page = cursor.getInt(QUOTE_PAGE)
        q.extra1 = cursor.getString(QUOTE_EXTRA1)
        q.extra2 = cursor.getString(QUOTE_EXTRA2)
        q.extra3 = cursor.getString(QUOTE_EXTRA3)
        return q
    }

    override fun toContentValues(q: T): ContentValues {
        return contentValues {
            put(QUOTE_PUBLIC_ID, q.publicId)
            put(QUOTE_DATE, q.date)
            put(QUOTE_IS_NEW, booleanToInt(q.isNew))
            put(QUOTE_TEXT, q.text)
            put(QUOTE_RATING, q.rating)
            put(QUOTE_FLAG, q.flag)
            put(QUOTE_PAGE, q.page)
            put(QUOTE_EXTRA1, q.extra1)
            put(QUOTE_EXTRA2, q.extra2)
            put(QUOTE_EXTRA3, q.extra3)
        }
    }

    protected abstract fun newInstance(): T

    protected fun intToBoolean(value: Int?) = when (value) {
        null -> null
        else -> value == 1
    }

    protected fun booleanToInt(value: Boolean?): Int = when (value) {
        null -> 1
        else -> if (value) 1 else 0
    }
}

class IndexedMapper : AbstractMapper<Quote.Indexed>() {

    override fun newInstance(): Quote.Indexed = Quote.Indexed()
}

class ShuffledMapper : AbstractMapper<Quote.Shuffled>() {

    override fun newInstance(): Quote.Shuffled = Quote.Shuffled()
}