package ru.aim.anotheryetbashclient.async;

public interface IDataReceiver {

    void onData(ISimpleCommand command);

    void onError(ISimpleCommand command);
}
