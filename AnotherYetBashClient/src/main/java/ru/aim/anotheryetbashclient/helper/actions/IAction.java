package ru.aim.anotheryetbashclient.helper.actions;

import android.os.Bundle;

public interface IAction {

    void execute() throws Exception;

    Bundle getArguments();
}
