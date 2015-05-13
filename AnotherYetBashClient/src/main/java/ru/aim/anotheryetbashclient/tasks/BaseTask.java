package ru.aim.anotheryetbashclient.tasks;

import ru.aim.anotheryetbashclient.BashApp;
import ru.aim.anotheryetbashclient.async.AbstractCommand;
import ru.aim.anotheryetbashclient.helper.DbHelper;
import ru.aim.anotheryetbashclient.network.INetworkApi;

public abstract class BaseTask extends AbstractCommand {

    INetworkApi getNetworkApi() {
        return BashApp.getInstance().getDi().networkApi();
    }

    DbHelper getDbHelper() {
        return new DbHelper(getContext());
    }
}
