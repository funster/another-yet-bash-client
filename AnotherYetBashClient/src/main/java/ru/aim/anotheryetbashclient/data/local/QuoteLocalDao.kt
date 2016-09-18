package ru.aim.anotheryetbashclient.data.local

import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import ru.aim.anotheryetbashclient.data.QuoteTypes
import java.util.*

class QuoteLocalDao(private val context: Context) {

    private val tables: List<String>
    private val sqlHelper: SQLiteOpenHelper
    private val daos = HashMap<QuoteTypes, QuoteDao>()

    init {
        tables = QuoteTypes.values().map { generateTableName(it.name) }
        sqlHelper = QuotesSQLiteHelper(context, tables)
    }

    private fun generateTableName(name: String): String = "${name.toLowerCase()}_table"

    fun getDaoForType(type: QuoteTypes): QuoteDao {
        return daos.getOrPut(type, { QuoteSqlDao(generateTableName(type.name), sqlHelper.writableDatabase) })
    }
}