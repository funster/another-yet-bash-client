package ru.aim.anotheryetbashclient.support.view

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.CoordinatorLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import ru.aim.anotheryetbashclient.R
import ru.aim.anotheryetbashclient.helper.L
import ru.aim.anotheryetbashclient.view.inflate
import ru.aim.anotheryetbashclient.view.setTextOrGone
import java.util.*

class BottomListBuilder(val context: Context) {

    private val TAG = "BottomListBuilder"

    private val items = ArrayList<Item>()
    var header: CharSequence? = null
        private set
    var listener: ((Item, Int) -> Unit)? = null // on item click listener (item, position) -> Unit
        private set

    fun item(item: Item): BottomListBuilder {
        items.add(item)
        return this
    }

    fun header(header: CharSequence?): BottomListBuilder {
        this.header = header
        return this
    }

    fun header(@StringRes header: Int): BottomListBuilder {
        return header(context.getString(header))
    }

    fun item(id: Int, text: CharSequence): BottomListBuilder {
        items.add(Item(id, text))
        return this
    }

    fun listener(listener: (Item, Int) -> Unit): BottomListBuilder {
        this.listener = listener
        return this
    }

    fun item(id: Int, @StringRes header: Int, @DrawableRes ico: Int): BottomListBuilder {
        return item(Item(id, context.getString(header), null, ico))
    }

    fun create(): BottomSheetDialog {
        if (items.isEmpty()) {
            throw IllegalArgumentException("At least one item in list should be provided!")
        }

        val dialog = BottomSheetDialog(context)
        val view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_list, null)

        val headerView = view.findViewById(R.id.header) as TextView
        headerView.setTextOrGone(header)

        val recyclerView = view.findViewById(R.id.recycler_view) as RecyclerView
        val linearManager = LinearLayoutManager(context)
        linearManager.isAutoMeasureEnabled = true
        recyclerView.layoutManager = linearManager
        recyclerView.adapter = BottomAdapter(items, { item: Item, i: Int ->
            if (listener != null) {
                dialog.dismiss()
                listener?.invoke(item, i)
            }
        })

        dialog.setContentView(view, FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT))
        val layoutParams = (view.parent as View).layoutParams as CoordinatorLayout.LayoutParams
        val behavior = layoutParams.behavior
        if (behavior is BottomSheetBehavior<View>) {
            behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        dialog.dismiss()
                    }
                }
            })
            Handler(Looper.getMainLooper()).postDelayed({
                Handler(Looper.getMainLooper()).postDelayed({
                    try {
                        // see https://code.google.com/p/android/issues/detail?id=203114
                        val field = BottomSheetBehavior::class.java.getDeclaredField("mState");
                        field.isAccessible = true;
                        field.setInt(behavior, BottomSheetBehavior.STATE_EXPANDED);
                    } catch (e: NoSuchFieldException) {
                        L.e(TAG, "Error setting BottomSheetBehavior initial state (1)", e);
                    } catch (e: IllegalAccessException) {
                        L.e(TAG, "Error setting BottomSheetBehavior initial state (2)", e);
                    }
                    behavior.peekHeight = view.height
                }, 100)
                behavior.peekHeight = view.height
            }, 100)
        }
        return dialog
    }

    fun show() {
        create().show()
    }

    class Item(val id: Int, val header: CharSequence, val subHeader: CharSequence? = null, @DrawableRes val ico: Int = 0)

    private class ItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

        val headerView: TextView?
        val subHeaderView: TextView?
        val imageView: ImageView?

        init {
            headerView = itemView?.findViewById(R.id.bs_list_title) as TextView
            subHeaderView = itemView?.findViewById(R.id.bs_list_sub_title) as TextView
            imageView = itemView?.findViewById(R.id.bs_image) as ImageView
        }

        fun bind(item: Item?, listener: ((Item, Int) -> Unit)?) {
            if (item == null) return // summon smart cast
            headerView.setTextOrGone(item.header)
            subHeaderView.setTextOrGone(item.subHeader)
            imageView?.setImageResource(item.ico)
            if (listener != null) {
                itemView.setOnClickListener {
                    listener.invoke(item, adapterPosition)
                }
            }
        }
    }

    private class BottomAdapter(val items: ArrayList<Item>, val listener: ((Item, Int) -> Unit)?) : RecyclerView.Adapter<ItemViewHolder>() {

        override fun onBindViewHolder(holder: ItemViewHolder?, position: Int) {
            holder?.bind(items[position], listener)
        }

        override fun onCreateViewHolder(parent: ViewGroup?, p1: Int): ItemViewHolder? {
            return ItemViewHolder(parent.inflate(R.layout.bottom_sheet_item))
        }

        override fun getItemCount(): Int = items.size
    }
}