package ru.aim.anotheryetbashclient.helper;

import android.content.Intent;
import ru.aim.anotheryetbashclient.ActionsAndIntents;
import ru.aim.anotheryetbashclient.helper.f.Action;
import ru.aim.anotheryetbashclient.helper.impl.BashBestAction;
import ru.aim.anotheryetbashclient.helper.impl.BashNewAction;
import ru.aim.anotheryetbashclient.helper.impl.BashRandomAction;
import ru.aim.anotheryetbashclient.helper.impl.BashRulezAction;

import static ru.aim.anotheryetbashclient.helper.Preconditions.notNull;

public final class ActionRequestFactory {

    private ActionRequestFactory() {
        throw new AssertionError();
    }

    public static Action getQuiteRequest(Intent intent) {
        int typeId = intent.getIntExtra(ActionsAndIntents.TYPE_ID, 0);
        String quoteId = intent.getStringExtra(ActionsAndIntents.QUOTE_ID);
        switch (typeId) {
            case 0:
                return new BashNewAction();
            case 1:
                return new BashRandomAction();
            case 2:
                return new BashBestAction();
            case 3:
                notNull(quoteId);
                return new BashRulezAction(BashRulezAction.Type.RULEZ, quoteId);
            case 4:
                notNull(quoteId);
                return new BashRulezAction(BashRulezAction.Type.SUX, quoteId);
            default:
                throw new AssertionError();
        }
    }
}
