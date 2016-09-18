package ru.aim.anotheryetbashclient.data.local

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import java.io.Closeable
import java.util.*

inline fun SQLiteDatabase.inTransaction(f: SQLiteDatabase.() -> Unit) {
    this.beginTransaction()
    try {
        f(this)
        this.setTransactionSuccessful()
    } finally {
        this.endTransaction()
    }
}

inline fun <T> Cursor?.firstOrNull(f: Cursor.() -> T): T? {
    if (this == null) {
        return null
    }
    try {
        moveToFirst()
        return f(this)
    } finally {
        close()
    }
}

inline fun <T> Cursor?.mapWithClose(f: Cursor.() -> T): List<T> {
    val list = ArrayList<T>()
    if (this == null) {
        return list
    }
    try {
        while (moveToNext()) {
            list.add(f())
        }
    } finally {
        close()
    }
    return list
}

inline fun <T : Closeable> using(closeable: T?, f: T.() -> Unit) {
    if (closeable == null) {
        return
    }
    try {
        f(closeable)
    } finally {
        closeable.close()
    }
}

inline fun contentValues(f: ContentValues.() -> Unit): ContentValues {
    val values = ContentValues()
    f(values)
    return values
}

fun Cursor?.getString(row: String) = this?.getString(getColumnIndex(row))

fun Cursor?.getLong(row: String) = this?.getLong(getColumnIndex(row))

fun Cursor?.getInt(row: String) = this?.getInt(getColumnIndex(row))