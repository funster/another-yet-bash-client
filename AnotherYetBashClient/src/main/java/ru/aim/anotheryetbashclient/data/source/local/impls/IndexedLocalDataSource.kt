package ru.aim.anotheryetbashclient.data.source.local.impls

import ru.aim.anotheryetbashclient.data.Quote
import ru.aim.anotheryetbashclient.data.source.local.mapper.IndexedMapper
import ru.aim.anotheryetbashclient.data.source.local.tableName

class IndexedLocalDataSource : AbstractLocalDataSource<Quote.Indexed>(tableName<Quote.Indexed>(), IndexedMapper())