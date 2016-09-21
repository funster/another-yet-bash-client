package ru.aim.anotheryetbashclient.data.source.local

import android.database.Cursor
import com.squareup.sqlbrite.BriteDatabase
import ru.aim.anotheryetbashclient.data.Quote
import ru.aim.anotheryetbashclient.data.source.QuoteDataSource
import rx.Observable
import rx.functions.Func1

class QuoteSqlDao(private val tableName: String, override val db: BriteDatabase) : QuoteDataSource, SQLiteAware {

    private val defaultWhereClause = "$_ID = ?"
    private val selectAll = "select * from $tableName"
    private val selectById = "select * from $tableName where $defaultWhereClause"
    private val selectByPage = "select * from $tableName where $QUOTE_PAGE = ?"

    private val cursorMapper = QuoteCursorMapper()
    private val mapper: Func1<Cursor, Quote> = Func1 { cursorMapper.fromCursor(it) }

    override fun findByPage(page: Int): Observable<List<Quote>> {
        return db.createQuery(tableName, selectByPage, page.toString()).mapToList(mapper)
    }

    override fun findById(id: Long): Observable<Quote?> {
        return db.createQuery(tableName, selectById, id.toString()).mapToOneOrDefault(mapper, null)
    }

    override fun findAll(): Observable<List<Quote>> {
        return db.createQuery(tableName, selectAll, null).mapToList(mapper)
    }

    override fun save(list: List<Quote>) {
        db.inTransaction { list.forEach { doSave(this, it) } }
    }

    override fun delete(q: Quote) {
        val id = q.id
        if (id != null) {
            deleteById(id)
        }
    }

    override fun deleteById(id: Long) {
        db.delete(tableName, defaultWhereClause, id.toString())
    }

    override fun delete(list: List<Quote>) {
        db.inTransaction { list.forEach { delete(it) } }
    }

    private fun doSave(database: BriteDatabase, q: Quote) {
        val isUpdate = q.id != null
        val cv = cursorMapper.toContentValues(q)
        if (isUpdate) {
            database.update(tableName, cv, "$_ID = ?", q.id?.toString())
        } else {
            database.insert(tableName, cv)
        }
    }

    override fun save(q: Quote) {
        doSave(db, q)
    }
}