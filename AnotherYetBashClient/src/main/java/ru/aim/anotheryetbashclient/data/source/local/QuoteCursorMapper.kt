package ru.aim.anotheryetbashclient.data.source.local

import android.content.ContentValues
import android.database.Cursor
import ru.aim.anotheryetbashclient.data.Quote

class QuoteCursorMapper {

    fun fromCursor(cursor: Cursor): Quote {
        return Quote(
                cursor.getLong(_ID),
                cursor.getString(QUOTE_TEXT),
                cursor.getString(QUOTE_PUBLIC_ID),
                cursor.getString(QUOTE_RATING),
                cursor.getString(QUOTE_DATE),
                intToBoolean(cursor.getInt(QUOTE_IS_NEW)),
                cursor.getInt(QUOTE_FLAG),
                cursor.getInt(QUOTE_PAGE),
                cursor.getString(QUOTE_EXTRA1),
                cursor.getString(QUOTE_EXTRA2),
                cursor.getString(QUOTE_EXTRA3)
        )
    }

    fun toContentValues(q: Quote): ContentValues {
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

    private fun intToBoolean(value: Int?) = when (value) {
        null -> null
        else -> value == 1
    }

    private fun booleanToInt(value: Boolean?): Int = when (value) {
        null -> 1
        else -> if (value) 1 else 0
    }
}