package ru.aim.anotheryetbashclient

import android.app.Application

import ru.aim.anotheryetbashclient.data.DaggerDataComponent
import ru.aim.anotheryetbashclient.data.DataComponent
import ru.aim.anotheryetbashclient.data.source.local.LocalDataSourcesModule
import ru.aim.anotheryetbashclient.data.source.remote.RemoteDataSourceModule
import ru.aim.anotheryetbashclient.data.source.remote.parser.ParserModule

class BashApp : Application() {

    lateinit var dataComponent: DataComponent

    override fun onCreate() {
        super.onCreate()

        val appModule = ApplicationModule(this)
        val local = LocalDataSourcesModule()
        val remote = RemoteDataSourceModule()
        val parser = ParserModule()

        dataComponent = DaggerDataComponent
                .builder()
                .applicationModule(appModule)
                .localDataSourcesModule(local)
                .remoteDataSourceModule(remote)
                .parserModule(parser)
                .build()

        println(dataComponent)
    }
}