package ru.aim.anotheryetbashclient.async.commands;

import java.io.Serializable;

public interface ISimpleCommand<T> extends Serializable, ExceptionAware {

    T execute();
}
