package ru.aim.anotheryetbashclient.view

import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.ViewGroup
import android.widget.FrameLayout
import ru.aim.anotheryetbashclient.R

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val drawer = findViewById(R.id.drawer) as DrawerLayout

        val mainFrameLayout = findViewById(R.id.main_frame) as FrameLayout

        // drawer layout treats fitsSystemWindows specially so we have to handle insets ourselves
        drawer.setOnApplyWindowInsetsListener { v, insets ->
            val lpToolbar = toolbar.layoutParams as ViewGroup.MarginLayoutParams
            // inset the toolbar down by the status bar height
            lpToolbar.topMargin += insets.systemWindowInsetTop
            lpToolbar.rightMargin += insets.systemWindowInsetRight
            toolbar.layoutParams = lpToolbar

            // inset the grid top by statusbar+toolbar & the bottom by the navbar (don't clip)
//            grid.setPadding(grid.getPaddingLeft(),
//                    insets.systemWindowInsetTop + ViewUtils.getActionBarSize(this@HomeActivity),
//                    grid.getPaddingRight() + insets.systemWindowInsetRight, // landscape
//                    grid.getPaddingBottom() + insets.systemWindowInsetBottom)

            // inset the fab for the navbar
//            fab.getLayoutParams().bottomMargin += insets.systemWindowInsetBottom // portrait
//            fab.getLayoutParams().rightMargin += insets.systemWindowInsetRight // landscape
//            fab.setLayoutParams(fab.getLayoutParams())

//            val postingStub = findViewById(R.id.stub_posting_progress)
//            val lpPosting = postingStub?.layoutParams as ViewGroup.MarginLayoutParams
//            lpPosting.bottomMargin += insets.systemWindowInsetBottom // portrait
//            lpPosting.rightMargin += insets.systemWindowInsetRight // landscape
//            postingStub.setLayoutParams(lpPosting)

            // we place a background behind the status bar to combine with it's semi-transparent
            // color to get the desired appearance.  Set it's height to the status bar height
//            val statusBarBackground = findViewById(R.id.status_bar_background)
//            val lpStatus = statusBarBackground?.layoutParams as FrameLayout.LayoutParams
//            lpStatus.height = insets.systemWindowInsetTop
//            statusBarBackground?.layoutParams = lpStatus

            // inset the filters list for the status bar / navbar
            // need to set the padding end for landscape case
//            val ltr = filtersList.getLayoutDirection() == View.LAYOUT_DIRECTION_LTR
//            filtersList.setPaddingRelative(filtersList.getPaddingStart(),
//                    filtersList.getPaddingTop() + insets.systemWindowInsetTop,
//                    filtersList.getPaddingEnd() + if (ltr)
//                        insets.systemWindowInsetRight
//                    else
//                        0,
//                    filtersList.getPaddingBottom() + insets.systemWindowInsetBottom)

            // clear this listener so insets aren't re-applied
            drawer.setOnApplyWindowInsetsListener(null)

            insets.consumeSystemWindowInsets()
        }

        if (savedInstanceState == null) {
            supportFragmentManager.
                    beginTransaction().
                    add(R.id.main_frame, NewFragment()).
                    commit()
        }
    }
}