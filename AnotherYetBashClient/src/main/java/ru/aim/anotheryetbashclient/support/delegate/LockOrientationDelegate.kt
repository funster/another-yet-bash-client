package ru.aim.anotheryetbashclient.support.delegate

import android.content.Context
import android.content.pm.ActivityInfo
import android.view.Surface
import android.view.WindowManager
import ru.aim.anotheryetbashclient.settings.SettingsHelper

class LockOrientationDelegate(val delegate: ActivityDelegate) : ActivityDelegate by delegate {

    override fun onResume() {
        provideOrientation()
    }

    protected fun provideOrientation() {
        if (SettingsHelper.isChangeOrientationEnabled(activity)) {
            lockScreen()
        } else {
            unlockScreen()
        }
    }

    protected fun lockScreen() {
        @SuppressWarnings("UnusedAssignment")
        var orientation: Int
        val rotation = (activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.rotation
        when (rotation) {
            Surface.ROTATION_0 -> orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            Surface.ROTATION_90 -> orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            Surface.ROTATION_180 -> orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
            else -> orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
        }

        activity.requestedOrientation = orientation
    }

    protected fun unlockScreen() {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }
}