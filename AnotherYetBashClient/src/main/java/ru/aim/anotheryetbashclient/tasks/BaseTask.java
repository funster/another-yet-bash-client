package ru.aim.anotheryetbashclient.tasks;

import android.content.Context;

import org.apache.http.client.HttpClient;

import ru.aim.anotheryetbashclient.BashApplication;
import ru.aim.anotheryetbashclient.async.AbstractCommand;
import ru.aim.anotheryetbashclient.helper.DbHelper;

import static ru.aim.anotheryetbashclient.helper.Preconditions.notNull;

public abstract class BaseTask extends AbstractCommand {

    HttpClient getHttpClient() {
        Context context = getContext();
        notNull(context);
        BashApplication app = (BashApplication) context.getApplicationContext();
        return app.getHttpClient();
    }

    DbHelper getDbHelper() {
        Context context = getContext();
        notNull(context);
        return new DbHelper(context);
    }
}
