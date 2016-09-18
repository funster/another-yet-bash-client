package ru.aim.anotheryetbashclient.data.local

import android.provider.BaseColumns

object QuotePersistenceContract : BaseColumns {

    const val QUOTE_PUBLIC_ID = "quote_public_id"
    const val QUOTE_DATE = "quote_date"
    const val QUOTE_IS_NEW = "quote_is_new"
    const val QUOTE_FLAG = "quote_flag"
    const val QUOTE_TEXT = "quote_text"
    const val QUOTE_RATING = "quote_rating"
    const val QUOTE_TYPE = "quote_type"
    const val QUOTE_PAGE = "quote_page"

}