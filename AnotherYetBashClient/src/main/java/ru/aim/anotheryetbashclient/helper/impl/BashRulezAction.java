package ru.aim.anotheryetbashclient.helper.impl;

import android.content.Context;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.support.v4.content.LocalBroadcastManager;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import ru.aim.anotheryetbashclient.ActionsAndIntents;
import ru.aim.anotheryetbashclient.R;
import ru.aim.anotheryetbashclient.helper.ContextAware;
import ru.aim.anotheryetbashclient.helper.HttpAware;
import ru.aim.anotheryetbashclient.helper.L;
import ru.aim.anotheryetbashclient.helper.Utils;
import ru.aim.anotheryetbashclient.helper.f.Action;
import ru.aim.anotheryetbashclient.helper.f.Block;
import ru.aim.anotheryetbashclient.helper.multipart.MultipartEntity;
import ru.aim.anotheryetbashclient.helper.multipart.Part;
import ru.aim.anotheryetbashclient.helper.multipart.StringPart;

import static ru.aim.anotheryetbashclient.helper.Preconditions.notNull;
import static ru.aim.anotheryetbashclient.helper.Utils.*;

public class BashRulezAction implements HttpAware, ContextAware, Action {

    public static final String TAG = "BashRulezAction";
    public static final String REQUEST = "http://bash.im/quote/%s/%s";

    final Type type;
    final String quoteId;

    AndroidHttpClient httpClient;
    Context context;

    public BashRulezAction(Type type, String quoteId) {
        notNull(type);
        notNull(quoteId);
        this.type = type;
        this.quoteId = quoteId.replace("#", "");
    }

    @Override
    public void apply() {
        rethrowWithRuntime(new Block() {
            @Override
            public void apply() throws Exception {
                HttpPost post = new HttpPost(String.format(REQUEST, quoteId, type.name));
                StringPart quotePart = new StringPart("quote", quoteId, UTF_8);
                StringPart act = new StringPart("act", type.name, UTF_8);
                MultipartEntity entity = new MultipartEntity(new Part[]{act, quotePart});
                post.setEntity(entity);
                HttpResponse httpResponse = httpClient.execute(post);
                String result = readFromStream(httpResponse.getEntity().getContent());
                if ("OK".equalsIgnoreCase(result)) {
                    String text = context.getString(R.string.rulez_confirm, type == Type.RULEZ ? ""
                            : context.getString(R.string.no), quoteId);
                    Utils.sendMessageIntent(context, text);
                } else {
                    L.w(TAG, "Like result returns: " + result);
                }
            }
        });
    }

    @Override
    public void setHttpClient(AndroidHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public void setContext(Context context) {
        this.context = context;
    }

    public enum Type {

        RULEZ("rulez"), SUX("sux");

        String name;

        Type(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
