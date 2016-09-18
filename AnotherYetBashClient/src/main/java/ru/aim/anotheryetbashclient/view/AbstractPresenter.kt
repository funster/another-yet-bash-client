package ru.aim.anotheryetbashclient.view

import org.simplepresenter.Presenter
import ru.aim.anotheryetbashclient.di.inject
import ru.aim.anotheryetbashclient.data.Quote
import ru.aim.anotheryetbashclient.service.SharingService

abstract class AbstractPresenter : Presenter() {

    private val sharingService: SharingService by inject()

    abstract fun onRefresh()

    fun onShare(quote: Quote) {
        applyViewState(ShareCommand(quote = quote))
    }

    fun onShare(shareType: ShareType, quote: Quote) {
        sharingService.share(shareType, quote)
    }
}