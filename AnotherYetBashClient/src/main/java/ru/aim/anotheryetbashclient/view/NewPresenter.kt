package ru.aim.anotheryetbashclient.view

import org.simplepresenter.commands.ProgressViewCommand

class NewPresenter : AbstractPresenter() {

    override fun onViewCreated() {
        load()
    }

    private fun load() {
        applyViewState(ProgressViewCommand.INSTANCE)
//        service.loadAndSaveAndGet()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe {
//                    applyViewState(SimpleDataCommand(it))
//                }
    }

    override fun onRefresh() {
        load()
    }

    override fun onViewDestroyed() {
        super.onViewDestroyed()
    }
}