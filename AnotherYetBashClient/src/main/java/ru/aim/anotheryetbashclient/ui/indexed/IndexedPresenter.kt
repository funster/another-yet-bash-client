package ru.aim.anotheryetbashclient.ui.indexed

import android.content.Context
import ru.aim.anotheryetbashclient.BashApp
import ru.aim.anotheryetbashclient.data.source.IndexedRepository
import ru.aim.anotheryetbashclient.ui.common.AbstractPresenter
import javax.inject.Inject

class IndexedPresenter(override var context: Context) : AbstractPresenter() {

    @set:Inject
    lateinit var repository: IndexedRepository

    override fun onViewCreated() {
        val dataComponent = (context.applicationContext as BashApp).dataComponent
        val component = DaggerIndexedComponent
                .builder()
                .dataComponent(dataComponent)
                .build()
        component.inject(this)
        println(repository)
    }

    override fun onRefresh() {
    }

    override fun onViewDestroyed() {
        super.onViewDestroyed()
    }
}