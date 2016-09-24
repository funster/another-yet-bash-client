package ru.aim.anotheryetbashclient.ui.common

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import ru.aim.anotheryetbashclient.R
import ru.aim.anotheryetbashclient.data.Quote
import ru.aim.anotheryetbashclient.ui.inflate

class QuotesAdapterKt(val f: ((View?, Quote) -> Unit)? = null) : RecyclerView.Adapter<QuotesViewHolder>() {

    var list: List<Quote> = emptyList()

    override fun onBindViewHolder(holder: QuotesViewHolder?, position: Int) {
        holder?.bind(list[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): QuotesViewHolder? {
        return QuotesViewHolder(parent.inflate(R.layout.list_item), f)
    }

    override fun getItemCount(): Int = list.size
}