package ru.aim.anotheryetbashclient

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule(private val mContext: Context) {

    @Provides
    fun provideContext(): Context = mContext
}