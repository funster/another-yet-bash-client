package ru.aim.anotheryetbashclient.data.source.local.impls

import ru.aim.anotheryetbashclient.data.Quote
import ru.aim.anotheryetbashclient.data.source.local.mapper.IndexedMapper
import ru.aim.anotheryetbashclient.data.source.local.tableName

class ShuffleLocalDataSource : AbstractLocalDataSource<Quote.Indexed>(tableName<Quote.Shuffled>(), IndexedMapper())