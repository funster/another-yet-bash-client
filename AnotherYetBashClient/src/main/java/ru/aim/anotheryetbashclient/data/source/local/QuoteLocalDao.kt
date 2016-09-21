package ru.aim.anotheryetbashclient.data.source.local

import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import com.squareup.sqlbrite.BriteDatabase
import com.squareup.sqlbrite.BuildConfig
import ru.aim.anotheryetbashclient.data.QuoteTypes
import ru.aim.anotheryetbashclient.data.source.QuoteDataSource
import rx.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

class QuoteLocalDao @Inject constructor(context: Context) {

    private val tables: List<String>
    private val sqlHelper: SQLiteOpenHelper
    private val daoList = HashMap<QuoteTypes, QuoteDataSource>()
    private val briteDatabase: BriteDatabase

    init {
        tables = QuoteTypes.values().map { generateTableName(it.name) }
        sqlHelper = QuotesSQLiteHelper(context, tables)
        briteDatabase = sqlBrite.wrapDatabaseHelper(sqlHelper, Schedulers.io())
        briteDatabase.setLoggingEnabled(BuildConfig.DEBUG)
    }

    private fun generateTableName(name: String): String = "${name.toLowerCase()}_table"

    fun getDaoForType(type: QuoteTypes): QuoteDataSource {
        return daoList.getOrPut(type, { QuoteSqlDao(generateTableName(type.name), briteDatabase) })
    }
}