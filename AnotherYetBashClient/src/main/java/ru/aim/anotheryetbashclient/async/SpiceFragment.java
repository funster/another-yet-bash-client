package ru.aim.anotheryetbashclient.async;

import android.support.v4.app.Fragment;

import com.octo.android.robospice.SpiceManager;

public class SpiceFragment extends Fragment {

    private final SpiceManager spiceManager = new SpiceManager(NetworkService.class);

    @Override
    public void onStart() {
        spiceManager.start(getActivity());
        super.onStart();
    }

    @Override
    public void onStop() {
        spiceManager.shouldStop();
        super.onStop();
    }

    public SpiceManager getSpiceManager() {
        return spiceManager;
    }
}
