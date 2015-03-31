package ru.aim.anotheryetbashclient.async;

import java.io.Serializable;

public interface ISimpleCommand extends Serializable, IErrorAware {

    void execute() throws Exception;
}