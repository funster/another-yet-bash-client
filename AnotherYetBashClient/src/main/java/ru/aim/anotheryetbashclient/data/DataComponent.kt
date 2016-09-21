package ru.aim.anotheryetbashclient.data

import dagger.Component
import ru.aim.anotheryetbashclient.ApplicationModule
import ru.aim.anotheryetbashclient.data.source.QuoteRepository
import javax.inject.Singleton

@Singleton
@Component(dependencies = arrayOf(ApplicationModule::class))
interface DataComponent {

    val quoteRepository: QuoteRepository
}