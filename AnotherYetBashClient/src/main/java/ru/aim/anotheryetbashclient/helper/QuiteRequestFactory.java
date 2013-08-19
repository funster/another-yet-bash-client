package ru.aim.anotheryetbashclient.helper;

import android.content.Intent;

import ru.aim.anotheryetbashclient.ActionsAndIntents;
import ru.aim.anotheryetbashclient.helper.impl.BashBestRequest;
import ru.aim.anotheryetbashclient.helper.impl.BashRandomRequest;

public final class QuiteRequestFactory {

    private QuiteRequestFactory() {
        throw new AssertionError();
    }

    public static QuiteRequest getQuiteRequest(Intent intent) {
        int typeId = intent.getIntExtra(ActionsAndIntents.TYPE_ID, 0);
        return buildRequest(typeId);
    }

    // todo: maybe we should cache this requests because they stateless
    static QuiteRequest buildRequest(int typeId) {
        switch (typeId) {
            case 0:
                return new BashRandomRequest();
            case 1:
                return new BashBestRequest();
            default:
                throw new AssertionError();
        }
    }
}
