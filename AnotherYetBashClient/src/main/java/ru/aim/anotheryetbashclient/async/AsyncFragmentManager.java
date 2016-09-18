package ru.aim.anotheryetbashclient.async;

import android.content.Context;

public class AsyncFragmentManager extends AsyncManager {

    private final AsyncFragment fragment;

    public AsyncFragmentManager(AsyncFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    Context getContext() {
        if (fragment != null && fragment.getActivity() != null)
            return fragment.getActivity();

        // Fallback
        return null;
    }

    @Override
    int getSourceKey() {
        return fragment.getClass().hashCode();
    }

    @Override
    IDataReceiver getDataReceiver() {
        return fragment;
    }
}