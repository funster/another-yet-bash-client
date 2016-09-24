package ru.aim.anotheryetbashclient.data.source.local

import com.squareup.sqlbrite.SqlBrite
import ru.aim.anotheryetbashclient.data.Quote

val sqlBrite = SqlBrite.create()

inline fun <reified T> tableName(): String where T : Quote = "${T::class.java.simpleName}_table"