package ru.aim.anotheryetbashclient.data

import rx.Observable

interface NewQuoteDataSource : QuoteDataSource {

    fun findByPage(page: Int): Observable<Quote>

    fun findAll(): Observable<Quote>
}