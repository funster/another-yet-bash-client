package ru.aim.anotheryetbashclient.ui

import android.support.annotation.StringRes
import ru.aim.anotheryetbashclient.R

enum class ShareType(@StringRes val res: Int) {
    DEFAULT(0),
    IMAGE(R.string.share_image),
    TEXT(R.string.share_text),
    LINK(R.string.share_link),
    BUFFER(R.string.share_buffer)
}