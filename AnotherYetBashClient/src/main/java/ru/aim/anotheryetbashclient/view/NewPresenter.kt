package ru.aim.anotheryetbashclient.view

import org.simplepresenter.commands.ProgressViewCommand
import org.simplepresenter.commands.SimpleDataCommand
import ru.aim.anotheryetbashclient.data.local.dao.NewQuotesDaoImpl
import ru.aim.anotheryetbashclient.parser.NewParser
import ru.aim.anotheryetbashclient.service.QuoteService
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class NewPresenter : AbstractPresenter() {

    val service = QuoteService(NewQuotesDaoImpl(), NewParser())

    override fun onViewCreated() {
        load()
    }

    private fun load() {
        applyViewState(ProgressViewCommand.INSTANCE)
        service.loadAndSaveAndGet()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    applyViewState(SimpleDataCommand(it))
                }
    }

    override fun onRefresh() {
        load()
    }

    override fun onViewDestroyed() {
        super.onViewDestroyed()
    }
}