package ru.aim.anotheryetbashclient.async;

import android.content.Context;

class AsyncFragmentManager extends AsyncManager {

    private final AsyncFragment fragment;

    public AsyncFragmentManager(AsyncFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    Context getContext() {
        return fragment.getActivity();
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