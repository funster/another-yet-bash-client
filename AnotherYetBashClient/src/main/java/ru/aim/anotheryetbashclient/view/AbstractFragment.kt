package ru.aim.anotheryetbashclient.view

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import org.simplepresenter.LcePresenterFragment
import org.simplepresenter.ViewCommand
import ru.aim.anotheryetbashclient.R
import ru.aim.anotheryetbashclient.support.view.BottomListBuilder

abstract class AbstractFragment<D, P : AbstractPresenter> : LcePresenterFragment<D, P>() {

    private val CONTENT = 0
    private val DATA = 1
    private val ERROR = 2

    var viewFlipper: ViewFlipper? = null
    var swipeRefreshLayout: SwipeRefreshLayout? = null
    var recyclerView: RecyclerView? = null

    var progressBar: ProgressBar? = null
    var emptyContainer: LinearLayout? = null

    var emptyText: TextView? = null
    var refreshButton: Button? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = container.inflate(R.layout.fragment_list2)
        viewFlipper = root?.findViewById(R.id.viewFlipper) as ViewFlipper
        viewFlipper?.inAnimation = AnimationUtils.loadAnimation(activity, android.R.anim.fade_in)
        viewFlipper?.outAnimation = AnimationUtils.loadAnimation(activity, android.R.anim.fade_out)
        viewFlipper?.animateFirstView = false

        swipeRefreshLayout = root?.findViewById(R.id.swipeContainer) as SwipeRefreshLayout
        swipeRefreshLayout?.setOnRefreshListener {
            presenter.onRefresh()
        }

        recyclerView = root?.findViewById(R.id.recyclerView) as RecyclerView
        recyclerView?.layoutManager = LinearLayoutManager(activity)
        recyclerView?.itemAnimator = DefaultItemAnimator()

        progressBar = root?.findViewById(R.id.progressBar) as ProgressBar
        emptyContainer = root?.findViewById(android.R.id.empty) as LinearLayout
        emptyText = root?.findViewById(R.id.emptyText) as TextView
        refreshButton = root?.findViewById(R.id.refreshButton) as Button
        return root
    }

    override fun onData(data: D) {
        super.onData(data)
        swipeRefreshLayout?.isRefreshing = false
        viewFlipper?.displayedChild = CONTENT
    }

    override fun onProgress() {
        super.onProgress()
        if (recyclerView?.adapter == null || recyclerView?.adapter?.itemCount == 0) {
            viewFlipper?.displayedChild = DATA
        }
    }

    override fun onError(throwable: Throwable?) {
        super.onError(throwable)
        swipeRefreshLayout?.isRefreshing = false
        viewFlipper?.displayedChild = ERROR
    }

    override fun onElse(viewCommand: ViewCommand?) {
        super.onElse(viewCommand)
        if (viewCommand is ShareCommand) {
            val builder = BottomListBuilder(activity)
            builder.header(R.string.share_desc)
            val arr = resources.getStringArray(R.array.share_types)
            arr.forEachIndexed { index, s -> builder.item(index, s) }
            builder.listener({ item, index ->
                presenter.onShare(ShareType.values()[index], viewCommand.quote)
            })
            builder.show()
        }
    }
}