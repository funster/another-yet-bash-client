package ru.aim.anotheryetbashclient.helper;

import android.content.Intent;
import ru.aim.anotheryetbashclient.helper.actions.*;
import ru.aim.anotheryetbashclient.helper.f.Action;

import static ru.aim.anotheryetbashclient.ActionsAndIntents.*;
import static ru.aim.anotheryetbashclient.helper.Preconditions.notNull;

public final class ActionRequestFactory {

    private ActionRequestFactory() {
        throw new AssertionError();
    }

    public static Action getQuiteRequest(Intent intent) {
        int typeId = intent.getIntExtra(TYPE_ID, 0);
        String quoteId = intent.getStringExtra(QUOTE_ID);
        switch (typeId) {
            case TYPE_NEW:
                return new BashFreshAction();
            case TYPE_RANDOM:
                return new BashRandomAction();
            case TYPE_BEST:
                return new BashBestAction();
            case TYPE_BY_RATING:
                return new BashByRating();
            case TYPE_ABYSS:
                return new AbyssAction();
            case TYPE_BEST_ABYSS:
                return new TopAbyssAction();
            case TYPE_TOP_ABYSS:
                return new BestAbyssAction();
            case TYPE_OFFLINE:
                return new OfflineAction();
            case TYPE_FAVORITES:
                return new FavoritesAction();
            case TYPE_RULEZ:
                notNull(quoteId);
                return new BashRulezAction(BashRulezAction.Type.RULEZ, quoteId);
            case TYPE_SUX:
                notNull(quoteId);
                return new BashRulezAction(BashRulezAction.Type.SUX, quoteId);
            case TYPE_SEARCH:
                return new BashSearchAction();
        }
        throw new AssertionError();
    }
}
