package ru.aim.anotheryetbashclient.async;

import android.content.Context;

public class AsyncActivityManager extends AsyncManager {

    final AsyncActivity activity;

    public AsyncActivityManager(AsyncActivity activity) {
        this.activity = activity;
    }

    @Override
    Context getContext() {
        return activity;
    }

    @Override
    int getSourceKey() {
        return activity.getClass().hashCode();
    }

    @Override
    IDataReceiver getDataReceiver() {
        return activity;
    }
}
