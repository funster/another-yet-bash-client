package ru.aim.anotheryetbashclient.async.commands;

public interface ExceptionAware {

    Exception getEx();

    void setEx(Exception ex);
}
