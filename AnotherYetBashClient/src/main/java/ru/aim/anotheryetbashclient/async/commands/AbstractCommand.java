package ru.aim.anotheryetbashclient.async.commands;

public abstract class AbstractCommand<T> implements ISimpleCommand<T> {

    Exception exception;

    @Override
    public Exception getEx() {
        return exception;
    }

    @Override
    public void setEx(Exception ex) {
        this.exception = ex;
    }
}
