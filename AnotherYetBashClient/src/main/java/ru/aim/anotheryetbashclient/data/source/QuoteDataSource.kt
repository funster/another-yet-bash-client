package ru.aim.anotheryetbashclient.data.source

import ru.aim.anotheryetbashclient.data.Quote
import rx.Observable

interface QuoteDataSource {

    fun save(q: Quote)

    fun save(list: List<Quote>)

    fun findById(id: Long): Observable<Quote?>

    fun findByPage(page: Int): Observable<List<Quote>>

    fun findAll(): Observable<List<Quote>>

    fun delete(q: Quote)

    fun deleteById(id: Long)

    fun delete(list: List<Quote>)
}