package ru.aim.anotheryetbashclient.data.source.local

import com.squareup.sqlbrite.BriteDatabase

interface SQLiteAware {

    val db: BriteDatabase?
}