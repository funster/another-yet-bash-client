package ru.aim.anotheryetbashclient.data.source.remote

import dagger.Module
import dagger.Provides

@Module
class RemoteDataSourceModule {

    @Provides
    fun networkClient(): NetworkClient = SimpleNetworkClient()
}