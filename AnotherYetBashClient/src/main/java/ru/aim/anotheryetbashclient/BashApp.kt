package ru.aim.anotheryetbashclient

import android.app.Application

import ru.aim.anotheryetbashclient.data.DaggerDataComponent
import ru.aim.anotheryetbashclient.data.DataComponent

class BashApp : Application() {

    lateinit var dataComponent: DataComponent

    override fun onCreate() {
        super.onCreate()

        val appModule = ApplicationModule(this)

        dataComponent = DaggerDataComponent.builder()
                .applicationModule(appModule)
                .build()

        println(dataComponent)
    }
}