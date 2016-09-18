package ru.aim.anotheryetbashclient.async;

import ru.aim.anotheryetbashclient.async.commands.ISimpleCommand;

public interface IDataReceiver {

    void onData(ISimpleCommand command);

    void onError(ISimpleCommand command);
}
