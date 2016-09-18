package ru.aim.anotheryetbashclient.async;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import ru.aim.anotheryetbashclient.async.commands.ISimpleCommand;
import ru.aim.anotheryetbashclient.helper.L;

class DataStore {

    private static final String TAG = "DataStore";

    private static final List<SourceCommands> data1 = new LinkedList<>();

    static class SourceCommands {

        private final int sourceKey;
        private final List<CommandWrapper> commands = new ArrayList<>();

        public SourceCommands(int sourceKey) {
            this.sourceKey = sourceKey;
        }

        CommandWrapper find(int commandKey) {
            for (int i = 0; i < commands.size(); i++) {
                ISimpleCommand command = commands.get(i).command;
                if (commandKey == command.hashCode()) {
                    return commands.get(i);
                }
            }
            return null;
        }

        void add(CommandWrapper commandWrapper) {
            if (commands.contains(commandWrapper)) {
                commands.remove(commandWrapper);
            }
            commands.add(commandWrapper);
        }

        CommandWrapper remove(int commandKey) {
            CommandWrapper toRemove = null;
            for (int i = 0; i < commands.size(); i++) {
                CommandWrapper wrapper = commands.get(i);
                if (wrapper.command.hashCode() == commandKey) {
                    toRemove = wrapper;
                    break;
                }
            }
            if (toRemove != null) {
                L.d(TAG, "Removing command " + toRemove);
                commands.remove(toRemove);
            }
            return toRemove;
        }

        void cancelAll() {
            for (int i = 0; i < commands.size(); i++) {
                CommandWrapper wrapper = commands.get(i);
                wrapper.requestStatus = RequestStatus.CANCELED;
            }
        }

        boolean isEmpty() {
            return commands.isEmpty();
        }

        @Override
        public String toString() {
            return "Source commands: " + commands.toString();
        }
    }

    static SourceCommands getOrAdd(int sourceKey) {
        SourceCommands commands = findSourceCommands(sourceKey);
        if (commands == null) {
            commands = new SourceCommands(sourceKey);

            synchronized (data1) {
                data1.add(commands);
            }
        }
        return commands;
    }

    static CommandWrapper findCommand(RequestKey requestKey) {
        synchronized (data1) {
            SourceCommands sourceCommands = getOrAdd(requestKey.sourceKey);
            return sourceCommands.find(requestKey.commandKey);
        }
    }

    private static SourceCommands findSourceCommands(int sourceKey) {
        for (int i = 0; i < data1.size(); i++) {
            SourceCommands sc = data1.get(i);
            if (sc == null) {
                return null;
            }
            if (sc.sourceKey == sourceKey) {
                return sc;
            }
        }
        return null;
    }

    static CommandWrapper removeCommand(RequestKey requestKey) {
        SourceCommands commands = getOrAdd(requestKey.sourceKey);
        if (commands == null) {
            return null;
        }
        CommandWrapper wrapper = commands.remove(requestKey.commandKey);
        if (commands.isEmpty()) {
            synchronized (data1) {
                data1.remove(commands);
            }
        }
        return wrapper;
    }

    static void addCommand(RequestKey requestKey, CommandWrapper commandWrapper) {
        L.d(TAG, "Add request key" + requestKey + " command " + commandWrapper);
        SourceCommands sourceCommands = getOrAdd(requestKey.sourceKey);
        sourceCommands.add(commandWrapper);
    }

    static void cancelAll() {
        synchronized (data1) {
            for (int i = 0; i < data1.size(); i++) {
                SourceCommands sourceCommands = data1.get(i);
                sourceCommands.cancelAll();
            }
            data1.clear();
        }
        data1.clear();
    }

    static List<CommandWrapper> findBySource(int sourceKey) {
        SourceCommands commands = findSourceCommands(sourceKey);
        if (commands == null) {
            return Collections.emptyList();
        } else {
            return new ArrayList<>(commands.commands);
        }
    }
}
