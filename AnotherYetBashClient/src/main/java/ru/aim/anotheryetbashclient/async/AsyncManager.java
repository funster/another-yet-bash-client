package ru.aim.anotheryetbashclient.async;

import android.content.Context;
import android.content.Intent;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ru.aim.anotheryetbashclient.async.commands.ISimpleCommand;
import ru.aim.anotheryetbashclient.helper.L;

@SuppressWarnings("unused")
public abstract class AsyncManager {

    private static final String TAG = "AsyncManager";

    public static final String REQUEST_TYPE_KEY = "REQUEST_TYPE_KEY";
    public static final String COMMAND_KEY = "COMMAND_KEY";
    public static final String SOURCE_KEY = "SOURCE_KEY";
    public static final String ABORT_ALL_FLAG = "ABORT_ALL_FLAG";

    private final Set<RequestKey> requestKeys = new HashSet<RequestKey>();

    private boolean mRestoreOnResume = true;

    public <T extends ISimpleCommand> void initRequest(T command, T... commands) {
        doRequest(command, RequestType.INIT);
        //noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < commands.length; i++) {
            doRequest(commands[i], RequestType.INIT);
        }
    }

    private <T extends ISimpleCommand> void doRequest(T command, RequestType requestType) {
        Intent intent = buildIntent(command);
        intent.putExtra(REQUEST_TYPE_KEY, requestType);
        getContext().startService(intent);
    }

    public <T extends ISimpleCommand> void forceRequest(T command, T... commands) {
        doRequest(command, RequestType.FORCE);
        //noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < commands.length; i++) {
            doRequest(commands[i], RequestType.FORCE);
        }
    }

    public void abortAll() {
        Intent intent = new Intent(getContext(), AsyncService.class);
        intent.putExtra(SOURCE_KEY, getSourceKey());
        intent.putExtra(ABORT_ALL_FLAG, true);
        getContext().startService(intent);
    }

    public static void abortAll(Context context) {
        Intent intent = new Intent(context, AsyncService.class);
        intent.putExtra(SOURCE_KEY, 0);
        intent.putExtra(ABORT_ALL_FLAG, true);
        context.startService(intent);
    }

    private Intent buildIntent(ISimpleCommand command) {
        requestKeys.add(new RequestKey(getRequestKey(command), getSourceKey()));
        return new Intent(getContext(), AsyncService.class).
                putExtra(COMMAND_KEY, command).
                putExtra(SOURCE_KEY, getSourceKey());
    }

    static int getRequestKey(ISimpleCommand command) {
        return command == null ? 0 : command.hashCode();
    }

    void onIntentReceived(Intent intent) {
        int commandKey = intent.getIntExtra(COMMAND_KEY, 0);
        RequestKey requestKey = new RequestKey(commandKey, getSourceKey());
        if (requestKeys.contains(requestKey)) {
            CommandWrapper commandWrapper = DataStore.removeCommand(requestKey);
            L.d(TAG, "Command wrapper for " + commandKey + " is " + commandWrapper);
            if (commandWrapper != null) {
                onCommandProceed(commandWrapper);
            }
        }
    }

    void onCommandProceed(CommandWrapper commandWrapper) {
        int commandKey = getRequestKey(commandWrapper.command);
        RequestKey requestKey = new RequestKey(commandKey, getSourceKey());
        DataStore.removeCommand(requestKey);
        ISimpleCommand command = commandWrapper.command;
        requestKeys.remove(requestKey);
        if (command == null) {
            L.d(TAG, "Command is null, nothing to notify");
        } else if (command.getEx() == null) {
            getDataReceiver().onData(command);
        } else {
            getDataReceiver().onError(command);
        }
    }

    public void setRestoreOnResume(boolean restoreOnResume) {
        this.mRestoreOnResume = restoreOnResume;
    }

    public void onResume() {
        if (!mRestoreOnResume) {
            return;
        }
        List<CommandWrapper> list = DataStore.findBySource(getSourceKey());
        //noinspection StatementWithEmptyBody
        for (int i = 0; i < list.size(); i++) {
            CommandWrapper commandWrapper = list.get(i);
            if (commandWrapper.requestStatus == RequestStatus.FINISHED) {
                onCommandProceed(commandWrapper);
            }
        }
    }

    abstract Context getContext();

    abstract int getSourceKey();

    abstract IDataReceiver getDataReceiver();

    enum RequestType {
        INIT, FORCE
    }

    public static void sendBroadcast(Context context, ISimpleCommand command) {
        Intent intent = new Intent(context, AsyncService.class).putExtra(COMMAND_KEY, command);
        context.startService(intent);
    }
}
