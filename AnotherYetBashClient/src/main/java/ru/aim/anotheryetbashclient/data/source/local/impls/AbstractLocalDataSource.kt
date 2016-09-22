package ru.aim.anotheryetbashclient.data.source.local.impls

import android.database.Cursor
import com.squareup.sqlbrite.BriteDatabase
import ru.aim.anotheryetbashclient.data.Quote
import ru.aim.anotheryetbashclient.data.source.local.*
import ru.aim.anotheryetbashclient.data.source.local.mapper.CursorMapper
import rx.Observable
import rx.functions.Func1

abstract class AbstractLocalDataSource<T>(private val tableName: String,
                                          private val cursorMapper: CursorMapper<T>,
                                          override val db: BriteDatabase)
: LocalDataSource<T>, SQLiteAware where T : Quote {

    private val defaultWhereClause = "${_ID} = ?"
    private val selectAll = "select * from $tableName"
    private val selectById = "select * from $tableName where $defaultWhereClause"
    private val selectByPage = "select * from $tableName where ${QUOTE_PAGE} = ?"

    private val mapper: Func1<Cursor, T> = Func1 { cursorMapper.fromCursor(it) }

    override fun findByPage(page: Int): Observable<List<T>> {
        return db.createQuery(tableName, selectByPage, page.toString()).mapToList(mapper)
    }

    override fun findById(id: Long): Observable<T?> {
        return db.createQuery(tableName, selectById, id.toString()).mapToOneOrDefault(mapper, null)
    }

    override fun findAll(): Observable<List<T>> {
        return db.createQuery(tableName, selectAll, null).mapToList(mapper)
    }

    override fun save(list: List<T>) {
        db.inTransaction { list.forEach { doSave(this, it) } }
    }

    override fun delete(q: T) {
        val id = q.id
        if (id != null) {
            deleteById(id)
        }
    }

    override fun deleteById(id: Long) {
        db.delete(tableName, defaultWhereClause, id.toString())
    }

    override fun delete(list: List<T>) {
        db.inTransaction { list.forEach { delete(it) } }
    }

    private fun doSave(database: BriteDatabase, q: T) {
        val isUpdate = q.id != null
        val cv = cursorMapper.toContentValues(q)
        if (isUpdate) {
            database.update(tableName, cv, "${_ID} = ?", q.id?.toString())
        } else {
            database.insert(tableName, cv)
        }
    }

    override fun save(q: T) {
        doSave(db, q)
    }
}