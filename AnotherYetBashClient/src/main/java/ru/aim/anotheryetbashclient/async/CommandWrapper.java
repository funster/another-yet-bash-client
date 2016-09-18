package ru.aim.anotheryetbashclient.async;

import ru.aim.anotheryetbashclient.async.commands.ISimpleCommand;

public class CommandWrapper {

    final ISimpleCommand command;
    RequestStatus requestStatus = RequestStatus.STARTING;

    CommandWrapper(ISimpleCommand command) {
        this.command = command;
    }

    @Override
    public String toString() {
        return "CommandWrapper{" +
                "command=" + command +
                ", requestStatus=" + requestStatus +
                '}';
    }
}
