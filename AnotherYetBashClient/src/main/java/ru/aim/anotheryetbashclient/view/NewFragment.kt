package ru.aim.anotheryetbashclient.view

import android.view.View
import android.widget.PopupMenu
import org.simplepresenter.ViewCommand
import ru.aim.anotheryetbashclient.R
import ru.aim.anotheryetbashclient.data.Quote
import java.util.*

class NewFragment : AbstractFragment<List<Quote>, NewPresenter>() {

    override fun createPresenter(): NewPresenter = NewPresenter()

    var adapter: QuotesAdapterKt? = null

    val onItemClick: (View?, Quote) -> Unit = { view, quote ->
        QuoteOptionsMenu.showPopMenu(view, R.menu.quote_menu, PopupMenu.OnMenuItemClickListener {
            when (it.itemId) {
                R.id.share -> presenter.onShare(quote)
            }
            true
        })
    }

    override fun onData(data: List<Quote>) {
        super.onData(data)
        if (adapter == null) {
            adapter = QuotesAdapterKt(onItemClick)
            recyclerView?.adapter = adapter
        }
        adapter?.list = ArrayList(data)
        adapter?.notifyDataSetChanged()
    }

    override fun onElse(viewCommand: ViewCommand?) {
        super.onElse(viewCommand)
    }
}