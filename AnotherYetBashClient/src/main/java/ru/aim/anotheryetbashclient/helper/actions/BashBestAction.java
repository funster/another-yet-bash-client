package ru.aim.anotheryetbashclient.helper.actions;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import ru.aim.anotheryetbashclient.ActionsAndIntents;
import ru.aim.anotheryetbashclient.helper.BaseAction;
import ru.aim.anotheryetbashclient.helper.f.Block;
import ru.aim.anotheryetbashclient.helper.f.Function1;

import static ru.aim.anotheryetbashclient.helper.Utils.rethrowWithRuntime;
import static ru.aim.anotheryetbashclient.helper.actions.Package.ElementWrapper;
import static ru.aim.anotheryetbashclient.helper.actions.Package.parseDocument;
import static ru.aim.anotheryetbashclient.helper.actions.Package.storeInDb;

@SuppressWarnings("unused")
public class BashBestAction extends BaseAction {

    public static final String TAG = "BashBestAction";

    static final String URL = "http://bash.im/best";

    @Override
    public void apply() {
        rethrowWithRuntime(new Block() {
            @Override
            public void apply() throws Exception {
                HttpGet httpRequest = new HttpGet(URL);
                HttpResponse httpResponse = getHttpClient().execute(httpRequest);
                parseDocument(httpResponse.getEntity().getContent(), URL, new Function1<ElementWrapper, Void>() {
                    @Override
                    public Void apply(ElementWrapper arg0) {
                        storeInDb(getDbHelper(), arg0);
                        return null;
                    }
                });
                LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
                localBroadcastManager.sendBroadcast(new Intent(ActionsAndIntents.REFRESH));
            }
        });
    }
}
