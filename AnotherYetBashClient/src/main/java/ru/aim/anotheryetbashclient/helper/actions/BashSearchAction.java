package ru.aim.anotheryetbashclient.helper.actions;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.BasicHttpParams;
import ru.aim.anotheryetbashclient.ActionsAndIntents;
import ru.aim.anotheryetbashclient.helper.BaseAction;
import ru.aim.anotheryetbashclient.helper.f.Block;
import ru.aim.anotheryetbashclient.helper.f.Function1;

import java.util.ArrayList;

import static ru.aim.anotheryetbashclient.helper.Preconditions.notNull;
import static ru.aim.anotheryetbashclient.helper.Utils.rethrowWithRuntime;
import static ru.aim.anotheryetbashclient.helper.actions.Package.*;

/**
 *
 */
public class BashSearchAction extends BaseAction {

    static final String URL = "http://bash.im/index";

    @Override
    public void apply() {
        rethrowWithRuntime(new Block() {
            @Override
            public void apply() throws Exception {
                String search = getIntent().getStringExtra(ActionsAndIntents.SEARCH_QUERY);
                assert search != null;
                notNull(search);
                HttpClient httpClient = getHttpClient();
                HttpGet httpGet = new HttpGet(URL);
                BasicHttpParams httpParams = new BasicHttpParams();
                httpParams.setParameter("text", search);
                httpGet.setParams(httpParams);
                HttpResponse httpResponse = httpClient.execute(httpGet);

                final ArrayList<String> list = new ArrayList<String>();
                parseDocument(httpResponse.getEntity().getContent(), URL, new Function1<ElementWrapper, Void>() {
                    @Override
                    public Void apply(ElementWrapper arg0) {
                        list.add(arg0.first);
                        storeInDb(getDbHelper(), arg0);
                        return null;
                    }
                });

                Intent intent = new Intent(ActionsAndIntents.REFRESH);
                intent.putStringArrayListExtra(ActionsAndIntents.IDS, list);
                LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getContext());
                manager.sendBroadcast(intent);
            }
        });
    }
}
