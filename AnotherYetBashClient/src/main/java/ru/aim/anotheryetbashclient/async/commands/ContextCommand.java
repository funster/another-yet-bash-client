package ru.aim.anotheryetbashclient.async.commands;

import android.content.Context;

public abstract class ContextCommand<T> extends AbstractCommand<T> {

    Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }
}
