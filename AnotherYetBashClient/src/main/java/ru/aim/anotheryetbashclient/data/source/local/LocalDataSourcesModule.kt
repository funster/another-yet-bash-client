package ru.aim.anotheryetbashclient.data.source.local

import android.content.Context
import com.squareup.sqlbrite.BriteDatabase
import dagger.Module
import dagger.Provides
import ru.aim.anotheryetbashclient.ApplicationModule
import ru.aim.anotheryetbashclient.data.source.local.impls.IndexedLocalDataSource
import ru.aim.anotheryetbashclient.data.source.local.impls.ShuffleLocalDataSource
import rx.schedulers.Schedulers
import javax.inject.Singleton

@Module(includes = arrayOf(ApplicationModule::class))
class LocalDataSourcesModule {

    private val sources = listOf<LocalDataSource<*>>(
            IndexedLocalDataSource(),
            ShuffleLocalDataSource()
    )

    @Provides
    fun indexed(context: Context): IndexedLocalDataSource = applySource(context)

    @Provides
    fun shuffle(context: Context): ShuffleLocalDataSource = applySource(context)

    @Provides
    @Singleton
    fun briteDatabase(context: Context): BriteDatabase {
        val sqlHelper = QuotesSQLiteHelper(context, sources)
        val briteDatabase = sqlBrite.wrapDatabaseHelper(sqlHelper, Schedulers.io())
        sources.forEach { it.db = briteDatabase }
        return briteDatabase
    }

    inline fun <T> List<T>.findOrThrow(f: (T) -> Boolean) = this.find(f) ?: throw IllegalStateException()

    private inline fun <reified T> applySource(context: Context): T where T : LocalDataSource<*> {
        val source = sources.findOrThrow { it is T } as T
        source.db = briteDatabase(context)
        return source
    }
}