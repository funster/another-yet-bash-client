package ru.aim.anotheryetbashclient.ui

import android.support.annotation.MenuRes
import android.view.View
import android.widget.PopupMenu

object QuoteOptionsMenu {

    fun showPopMenu(anchor: View?, @MenuRes menuRes: Int, listener: PopupMenu.OnMenuItemClickListener) {
        if (anchor == null) {
            return
        }
        val popMenu = PopupMenu(anchor.context, anchor)
        popMenu.inflate(menuRes)
        popMenu.setOnMenuItemClickListener(listener)
        popMenu.show()
    }

    private fun showIcons(popMenu: PopupMenu) {
        val clazz = popMenu.javaClass
        val popUpField = clazz.getDeclaredField("mPopup")
        popUpField.isAccessible = true
        val helperObject = popUpField.get(popMenu)
        val helperClazz = helperObject.javaClass
        val setForceIcons = helperClazz.getMethod("setForceShowIcon", Boolean::class.java)
        setForceIcons.invoke(helperObject, true)
    }
}