package ru.aim.anotheryetbashclient

import android.app.Application
import ru.aim.anotheryetbashclient.di.Di
import ru.aim.anotheryetbashclient.parser.HtmlParser
import ru.aim.anotheryetbashclient.parser.NewParser
import ru.aim.anotheryetbashclient.service.SharingService
import ru.aim.anotheryetbashclient.service.SharingServiceImpl

class BashApp : Application() {

    override fun onCreate() {
        super.onCreate()

        Di.bindFor(SharingService::class.java).withSpecific(SharingServiceImpl(applicationContext))
        Di.bindFor(HtmlParser::class.java).withSpecific(NewParser())
    }
}


