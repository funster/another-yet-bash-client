package ru.aim.anotheryetbashclient.async;

public class CommandWrapper {
    final ISimpleCommand command;
    RequestStatus requestStatus = RequestStatus.STARTING;

    CommandWrapper(ISimpleCommand command) {
        this.command = command;
    }
}
