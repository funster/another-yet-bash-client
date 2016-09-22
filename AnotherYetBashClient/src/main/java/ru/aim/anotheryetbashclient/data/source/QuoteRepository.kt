package ru.aim.anotheryetbashclient.data.source

import ru.aim.anotheryetbashclient.data.source.remote.IndexedNetworkDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuoteRepository @Inject constructor(val networkDataSource: IndexedNetworkDataSource)