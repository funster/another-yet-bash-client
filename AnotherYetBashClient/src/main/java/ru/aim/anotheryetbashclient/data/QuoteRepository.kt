package ru.aim.anotheryetbashclient.data

import android.content.Context
import ru.aim.anotheryetbashclient.data.local.QuoteLocalDao

class QuoteRepository(private val context: Context) {

    val localDao = QuoteLocalDao(context)
}