package ru.aim.anotheryetbashclient.ui.common

import android.content.Context
import org.simplepresenter.Presenter
import ru.aim.anotheryetbashclient.data.Quote
import ru.aim.anotheryetbashclient.ui.ShareCommand
import ru.aim.anotheryetbashclient.ui.ShareType

abstract class AbstractPresenter() : Presenter() {

    abstract fun onRefresh()

    fun onShare(quote: Quote) {
        applyViewState(ShareCommand(quote = quote))
    }

    fun onShare(shareType: ShareType, quote: Quote) {
    }

    abstract var context: Context
}