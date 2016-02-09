package ru.aim.anotheryetbashclient.ext.db

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

fun Cursor?.getString(column: String): String? = this?.getString(getColumnIndex(column))

fun Cursor?.getLong(column: String): Long? = this?.getLong(getColumnIndex(column))

fun Cursor?.getInt(column: String): Int? = this?.getInt(getColumnIndex(column))

inline fun SQLiteDatabase.inTransaction(f: SQLiteDatabase.() -> Unit) {
    beginTransaction()
    try {
        f()
        setTransactionSuccessful()
    } finally {
        endTransaction()
    }
}

inline fun <T> Cursor?.withClose(f: Cursor.() -> T?): T? {
    if (this == null) {
        return null
    } else {
        try {
            return f()
        } finally {
            close()
        }
    }
}
