package ru.aim.anotheryetbashclient.data.source

import ru.aim.anotheryetbashclient.data.source.local.impls.IndexedLocalDataSource
import ru.aim.anotheryetbashclient.data.source.local.impls.ShuffleLocalDataSource
import ru.aim.anotheryetbashclient.data.source.remote.IndexedNetworkDataSource
import ru.aim.anotheryetbashclient.data.source.remote.ShuffleNetworkDataSource
import javax.inject.Inject

class IndexedRepository @Inject constructor(val network: IndexedNetworkDataSource, val local: IndexedLocalDataSource)

class ShuffleRepository @Inject constructor(val network: ShuffleNetworkDataSource, val local: ShuffleLocalDataSource)