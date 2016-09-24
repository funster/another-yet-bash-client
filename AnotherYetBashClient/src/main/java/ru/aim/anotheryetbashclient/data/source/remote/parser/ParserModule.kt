package ru.aim.anotheryetbashclient.data.source.remote.parser

import dagger.Module
import dagger.Provides

@Module
class ParserModule {

    @Provides
    fun indexedParser(): IndexedParser = IndexedParser()
}