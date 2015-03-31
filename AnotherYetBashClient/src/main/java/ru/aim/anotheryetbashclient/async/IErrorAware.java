package ru.aim.anotheryetbashclient.async;

public interface IErrorAware {

    Throwable getEx();

    void setEx(Throwable th);
}
