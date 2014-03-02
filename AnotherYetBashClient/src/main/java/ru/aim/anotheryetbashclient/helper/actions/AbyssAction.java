package ru.aim.anotheryetbashclient.helper.actions;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import org.jsoup.nodes.Document;
import ru.aim.anotheryetbashclient.ActionsAndIntents;
import ru.aim.anotheryetbashclient.helper.BaseAction;
import ru.aim.anotheryetbashclient.helper.Preconditions;
import ru.aim.anotheryetbashclient.helper.f.Function1;

import static java.lang.String.format;
import static ru.aim.anotheryetbashclient.helper.actions.Package.*;

public class AbyssAction extends BaseAction {

    static final String URL = "http://bash.im/abyss";
    static final String MORE_URL = "http://bash.im/abyss%s";

    @Override
    public void apply() {
        if (true) {
            Preconditions.notImplemented();
        }
        final Intent intent = new Intent(ActionsAndIntents.REFRESH);
        String url;
        if (getIntent().hasExtra(ActionsAndIntents.NEXT_PAGE)) {
            url = format(MORE_URL, getIntent().getStringExtra(ActionsAndIntents.NEXT_PAGE));
        } else {
            url = URL;
        }
        parseDocument(getHttpClient(), url, new Function1<Document, Void>() {
                    @Override
                    public Void apply(Document arg0) {
                        intent.putExtra(ActionsAndIntents.NEXT_PAGE, findMore(arg0));
                        return null;
                    }
                },
                new Function1<ElementWrapper, Void>() {
                    @Override
                    public Void apply(ElementWrapper arg0) {
                        storeInDb(getDbHelper(), arg0);
                        return null;
                    }
                }
        );
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.sendBroadcast(intent);
    }
}
