package ru.aim.anotheryetbashclient.data

import dagger.Component
import ru.aim.anotheryetbashclient.data.source.IndexedRepository
import ru.aim.anotheryetbashclient.data.source.ShuffleRepository
import ru.aim.anotheryetbashclient.data.source.local.LocalDataSourcesModule
import ru.aim.anotheryetbashclient.data.source.remote.RemoteDataSourceModule
import ru.aim.anotheryetbashclient.data.source.remote.parser.ParserModule

@Component(modules =
arrayOf(
        LocalDataSourcesModule::class,
        RemoteDataSourceModule::class,
        ParserModule::class
))
interface DataComponent {

    val indexedRepository: IndexedRepository

    val shuffleRepository: ShuffleRepository
}