package ru.aim.anotheryetbashclient.data.local

import android.database.sqlite.SQLiteDatabase

interface SQLiteDao {

    val db: SQLiteDatabase?
}