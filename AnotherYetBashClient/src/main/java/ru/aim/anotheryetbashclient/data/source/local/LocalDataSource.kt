package ru.aim.anotheryetbashclient.data.source.local

import ru.aim.anotheryetbashclient.data.Quote
import rx.Observable

interface LocalDataSource<T> : SQLiteAware where T : Quote {

    fun save(q: T)

    fun save(list: List<T>)

    fun findById(id: Long): Observable<T?>

    fun findByPage(page: Int): Observable<List<T>>

    fun findAll(): Observable<List<T>>

    fun delete(q: T)

    fun deleteById(id: Long)

    fun delete(list: List<T>)

    val tableName: String
}