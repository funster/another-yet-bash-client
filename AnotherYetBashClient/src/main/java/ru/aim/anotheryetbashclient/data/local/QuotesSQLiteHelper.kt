package ru.aim.anotheryetbashclient.data.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class QuotesSQLiteHelper(context: Context?, private val tables: List<String>) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        createTables(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) = recreate()

    fun dropTables(db: SQLiteDatabase?) = tables.forEach { db?.execSQL(dropTable(it)) }

    fun dropTables() = dropTables(writableDatabase)

    fun createTables(db: SQLiteDatabase?) = tables.forEach {
        db?.execSQL(createTableSql(it))
    }

    fun createTables() = createTables(writableDatabase)

    fun recreate() {
        val db = writableDatabase
        dropTables(db)
        createTables(db)
    }

    private fun createTableSql(tableName: String) = """
            CREATE TABLE $tableName
            (${QUOTE_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${QUOTE_PUBLIC_ID} TEXT,
            ${QUOTE_DATE} TEXT,
            ${QUOTE_IS_NEW} INTEGER,
            ${QUOTE_FLAG} INTEGER,
            ${QUOTE_TYPE} INTEGER,
            ${QUOTE_RATING} TEXT,
            ${QUOTE_PAGE} INTEGER,
            ${QUOTE_TEXT} TEXT,
            ${QUOTE_EXTRA1} TEXT,
            ${QUOTE_EXTRA2} TEXT,
            ${QUOTE_EXTRA3} TEXT)
            """

    private fun dropTable(tableName: String) = "drop table if exists $tableName"
}