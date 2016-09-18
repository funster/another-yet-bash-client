package ru.aim.anotheryetbashclient.view

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import ru.aim.anotheryetbashclient.R
import ru.aim.anotheryetbashclient.data.Quote

class QuotesViewHolder(view: View?, val f: ((View?, Quote) -> Unit)? = null) : RecyclerView.ViewHolder(view) {

    var date: TextView
    var id: TextView
    var text: TextView
    var isNew: TextView
    var quoteActions: ImageView? = null

    var quoteContainer: View?

    init {
        date = view?.findViewById(android.R.id.text1) as TextView
        id = view?.findViewById(android.R.id.text2) as TextView
        text = view?.findViewById(R.id.text) as TextView
        isNew = view?.findViewById(R.id.newQuote) as TextView
        quoteContainer = view?.findViewById(R.id.quote_container)
        quoteActions = view?.findViewById(R.id.quote_actions) as ImageView
    }

    fun bind(q: Quote) {
        date.text = q.date
        id.text = q.publicId
        text.text = q.text
        if (f != null) {
            quoteActions?.setOnClickListener { f.invoke(it, q) }
            quoteActions?.visibility = View.VISIBLE
        } else {
            quoteActions?.visibility = View.GONE
        }
    }
}