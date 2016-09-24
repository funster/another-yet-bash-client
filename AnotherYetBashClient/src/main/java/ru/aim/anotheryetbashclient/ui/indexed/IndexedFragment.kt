package ru.aim.anotheryetbashclient.ui.indexed

import android.view.View
import android.widget.PopupMenu
import org.simplepresenter.ViewCommand
import ru.aim.anotheryetbashclient.R
import ru.aim.anotheryetbashclient.data.Quote
import ru.aim.anotheryetbashclient.ui.QuoteOptionsMenu
import ru.aim.anotheryetbashclient.ui.common.AbstractFragment
import ru.aim.anotheryetbashclient.ui.common.QuotesAdapterKt
import java.util.*

class IndexedFragment : AbstractFragment<List<Quote>, IndexedPresenter>() {

    override fun createPresenter(): IndexedPresenter = IndexedPresenter(activity)

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