package ru.aim.anotheryetbashclient.view

import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

fun ViewGroup?.inflate(@LayoutRes resource: Int): View? {
    if (this == null) {
        return null
    } else {
        return LayoutInflater.from(this.context).inflate(resource, this, false)
    }
}

fun TextView?.setTextOrGone(text: CharSequence?) {
    if (this == null) return
    if (text.isNullOrEmpty()) {
        this.visibility = View.GONE
        this.text = ""
    } else {
        this.text = text
        this.visibility = View.VISIBLE
    }
}