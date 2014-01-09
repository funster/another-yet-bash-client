package ru.aim.anotheryetbashclient.helper;

import android.content.Intent;
import ru.aim.anotheryetbashclient.ActionsAndIntents;
import ru.aim.anotheryetbashclient.helper.f.Action;
import ru.aim.anotheryetbashclient.helper.impl.BashBestAction;
import ru.aim.anotheryetbashclient.helper.impl.BashRandomAction;
import ru.aim.anotheryetbashclient.helper.impl.BashRulezAction;

public final class ActionRequestFactory {

    private ActionRequestFactory() {
        throw new AssertionError();
    }

    public static Action getQuiteRequest(Intent intent) {
        int typeId = intent.getIntExtra(ActionsAndIntents.TYPE_ID, 0);
        String quoteId = intent.getStringExtra(ActionsAndIntents.QUOTE_ID);
        switch (typeId) {
            case 0:
                return new BashRandomAction();
            case 1:
                return new BashBestAction();
            case 2:
                return new BashRulezAction(BashRulezAction.Type.RULEZ, quoteId);
            case 3:
                return new BashRulezAction(BashRulezAction.Type.SUX, quoteId);
            default:
                throw new AssertionError();
        }
    }
}
