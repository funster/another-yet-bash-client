package ru.aim.anotheryetbashclient.async;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import ru.aim.anotheryetbashclient.BuildConfig;
import ru.aim.anotheryetbashclient.helper.L;

import static ru.aim.anotheryetbashclient.async.DataStore.addCommand;
import static ru.aim.anotheryetbashclient.async.DataStore.findCommand;

public class AsyncService extends IntentService {

    private static final String TAG = "AsyncService";

    public static String TASK_FINISHED_ACTION = BuildConfig.APPLICATION_ID + ".async.TASK_FINISHED_ACTION";

    public AsyncService() {
        super("AsyncService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent.hasExtra(AsyncManager.ABORT_ALL_FLAG)) {
            cancelAll();
            return;
        }
        AsyncManager.RequestType requestType = (AsyncManager.RequestType) intent.getSerializableExtra(AsyncManager.REQUEST_TYPE_KEY);
        ISimpleCommand sbolCommand = (ISimpleCommand) intent.getSerializableExtra(AsyncManager.COMMAND_KEY);

        int sourceKey = intent.getIntExtra(AsyncManager.SOURCE_KEY, 0);
        int commandKey = sbolCommand.hashCode();

        RequestKey requestKey;
        CommandWrapper commandWrapper;

        synchronized (AsyncService.class) {
            requestKey = new RequestKey(commandKey, sourceKey);
            if (requestType == AsyncManager.RequestType.FORCE) {
                cancel(requestKey);
            }
            commandWrapper = findCommand(requestKey);
            if (commandWrapper == null) {
                commandWrapper = new CommandWrapper(sbolCommand);
                addCommand(requestKey, commandWrapper);
            } else {
                if (commandWrapper.requestStatus == RequestStatus.EXECUTING) {
                    L.d(TAG, "Task in progress, do noting");
                    return;
                }
            }
        }
        execute(requestKey, commandWrapper);
    }

    void execute(RequestKey requestKey, CommandWrapper wrapper) {
        try {
            beforeExecute(wrapper.command);
            wrapper.command.execute();
            afterExecute(wrapper.command);
        } catch (Exception e) {
            wrapper.command.setEx(e);
        }
        synchronized (AsyncService.class) {
            if (wrapper.requestStatus == RequestStatus.CANCELED) {
                DataStore.removeCommand(requestKey);
            } else {
                wrapper.requestStatus = RequestStatus.FINISHED;
                notifyListeners(requestKey);
            }
        }
    }

    void beforeExecute(ISimpleCommand command) {
        if (command instanceof IContextCommand) {
            ((IContextCommand) command).setContext(getApplicationContext());
        }
    }

    void afterExecute(ISimpleCommand command) {
        if (command instanceof IContextCommand) {
            ((IContextCommand) command).setContext(null);
        }
    }

    void cancel(RequestKey key) {
        CommandWrapper commandWrapper = findCommand(key);
        if (commandWrapper != null) {
            commandWrapper.requestStatus = RequestStatus.CANCELED;
        }
        DataStore.removeCommand(key);
    }

    void cancelAll() {
        synchronized (AsyncService.class) {
            DataStore.cancelAll();
        }
    }

    void notifyListeners(RequestKey requestKey) {
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        Intent intent = new Intent(TASK_FINISHED_ACTION);
        intent.putExtra(AsyncManager.COMMAND_KEY, requestKey.commandKey);
        manager.sendBroadcast(intent);
        afterNotifySend(requestKey);
    }

    void afterNotifySend(RequestKey requestKey) {
        // in case there is no source then remove from data store
        // cause it seems to be broadcast
        if (requestKey.sourceKey == 0) {
            DataStore.removeCommand(requestKey);
        }
    }
}
