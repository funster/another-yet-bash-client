package ru.aim.anotheryetbashclient.helper;

import android.content.Intent;
import ru.aim.anotheryetbashclient.ActionsAndIntents;
import ru.aim.anotheryetbashclient.helper.f.Action;
import ru.aim.anotheryetbashclient.helper.impl.*;

import static ru.aim.anotheryetbashclient.helper.Preconditions.notNull;

public final class ActionRequestFactory {

    private ActionRequestFactory() {
        throw new AssertionError();
    }

    public static Action getQuiteRequest(Intent intent) {
        int typeId = intent.getIntExtra(ActionsAndIntents.TYPE_ID, 0);
        String quoteId = intent.getStringExtra(ActionsAndIntents.QUOTE_ID);
        switch (typeId) {
            case ActionsAndIntents.TYPE_NEW:
                return new BashNewAction();
            case ActionsAndIntents.TYPE_RANDOM:
                return new BashRandomAction();
            case ActionsAndIntents.TYPE_BEST:
                return new BashBestAction();
            case ActionsAndIntents.TYPE_BY_RATING:
                return new BashByRating();
            case ActionsAndIntents.TYPE_ABYSS:
                Preconditions.notImplemented();
                break;
            case ActionsAndIntents.TYPE_BEST_ABYSS:
                Preconditions.notImplemented();
                break;
            case ActionsAndIntents.TYPE_TOP_ABYSS:
                Preconditions.notImplemented();
                break;
            case ActionsAndIntents.TYPE_RULEZ:
                notNull(quoteId);
                return new BashRulezAction(BashRulezAction.Type.RULEZ, quoteId);
            case ActionsAndIntents.TYPE_SUX:
                notNull(quoteId);
                return new BashRulezAction(BashRulezAction.Type.SUX, quoteId);
        }
        throw new AssertionError();
    }
}
