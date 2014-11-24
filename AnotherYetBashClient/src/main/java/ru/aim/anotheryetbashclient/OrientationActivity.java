package ru.aim.anotheryetbashclient;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.view.Surface;
import android.view.WindowManager;

public abstract class OrientationActivity extends AbstractActivity {

    protected void provideOrientation() {
        if (SettingsHelper.isChangeOrientationEnabled(this)) {
            lockScreen();
        } else {
            unlockScreen();
        }
    }

    protected void lockScreen() {
        @SuppressWarnings("UnusedAssignment")
        int orientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
        int rotation = ((WindowManager) getSystemService(
                Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
        switch (rotation) {
            case Surface.ROTATION_0:
                orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                break;
            case Surface.ROTATION_90:
                orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                break;
            case Surface.ROTATION_180:
                orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                break;
            default:
                orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                break;
        }

        setRequestedOrientation(orientation);
    }

    protected void unlockScreen() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    @Override
    protected void onResume() {
        super.onResume();
        provideOrientation();
    }
}
