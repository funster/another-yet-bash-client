package ru.aim.anotheryetbashclient.data.local

import ru.aim.anotheryetbashclient.data.Quote

interface QuoteDao : SQLiteDao {

    fun save(q: Quote): Quote

    fun saveAll(list: List<Quote>)

    fun findById(id: Long): Quote?

    fun findAll(): List<Quote>
}