package ru.aim.anotheryetbashclient.async;

import android.content.Context;

public abstract class AbstractCommand implements ISimpleCommand, IContextCommand, IErrorAware {

    private Throwable ex;
    private transient Context context;

    @Override
    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public Throwable getEx() {
        return ex;
    }

    @Override
    public void setEx(Throwable th) {
        this.ex = th;
    }
}
