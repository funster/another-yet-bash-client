package ru.aim.anotheryetbashclient.data.source.local.impls

import com.squareup.sqlbrite.BriteDatabase
import ru.aim.anotheryetbashclient.data.Quote
import ru.aim.anotheryetbashclient.data.source.local.impls.AbstractLocalDataSource
import ru.aim.anotheryetbashclient.data.source.local.mapper.IndexedMapper

class IndexedLocalDataSource(db: BriteDatabase) : AbstractLocalDataSource<Quote.Indexed>("quote-indexed", IndexedMapper(), db)