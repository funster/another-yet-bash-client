package ru.aim.anotheryetbashclient.async;

import android.support.v4.app.Fragment;

import com.octo.android.robospice.SpiceManager;

/**
 * Pavel : 14.05.2015.
 */
public class SpiceFragment extends Fragment {

    private SpiceManager spiceManager;

    public SpiceManager getSpiceManager() {
        if (spiceManager == null) {
            if (getActivity() instanceof SpiceActivity) {
                spiceManager = ((SpiceActivity) getActivity()).getSpiceManager();
            }
        }
        return spiceManager;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        spiceManager = null;
    }
}
