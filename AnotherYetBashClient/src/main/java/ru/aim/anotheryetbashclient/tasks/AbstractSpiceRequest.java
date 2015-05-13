package ru.aim.anotheryetbashclient.tasks;

import ru.aim.anotheryetbashclient.async.OkHttpSpiceRequest;

public class AbstractSpiceRequest extends OkHttpSpiceRequest<String> {

    public AbstractSpiceRequest() {
        super(String.class);
    }

    @Override
    public String loadDataFromNetwork() throws Exception {
        return null;
    }
}
