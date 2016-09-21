package ru.aim.anotheryetbashclient.view

import org.simplepresenter.Presenter
import ru.aim.anotheryetbashclient.data.Quote

abstract class AbstractPresenter : Presenter() {

    abstract fun onRefresh()

    fun onShare(quote: Quote) {
        applyViewState(ShareCommand(quote = quote))
    }

    fun onShare(shareType: ShareType, quote: Quote) {
    }
}