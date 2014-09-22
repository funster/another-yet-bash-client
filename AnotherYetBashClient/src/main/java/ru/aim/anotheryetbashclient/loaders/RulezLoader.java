package ru.aim.anotheryetbashclient.loaders;

import android.content.Context;
import android.net.http.AndroidHttpClient;
import android.os.Bundle;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;

import ru.aim.anotheryetbashclient.BashApplication;
import ru.aim.anotheryetbashclient.helper.multipart.MultipartEntity;
import ru.aim.anotheryetbashclient.helper.multipart.Part;
import ru.aim.anotheryetbashclient.helper.multipart.StringPart;

import static ru.aim.anotheryetbashclient.helper.Utils.UTF_8;
import static ru.aim.anotheryetbashclient.helper.Utils.readFromStream;
import static ru.aim.anotheryetbashclient.helper.actions.Package.wrapWithRoot;

public class RulezLoader extends AbstractLoader<RulezLoader.RulezResult> {

    public static final String TAG = "BashRulezAction";
    public static final String REQUEST = wrapWithRoot("quote/%s/%s");

    String id;
    String type;

    public RulezLoader(Context context, Bundle bundle) {
        super(context);
        id = bundle.getString("id");
        type = bundle.getString("type");
    }

    public static Bundle buildBundle(String id, RulezType rulezType) {
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("type", rulezType.name);
        return bundle;
    }

    @Override
    public RulezResult doInBackground() throws Exception {
        HttpPost post = new HttpPost(String.format(REQUEST, id, type));
        StringPart quotePart = new StringPart("quote", id, UTF_8);
        StringPart act = new StringPart("act", type, UTF_8);
        MultipartEntity entity = new MultipartEntity(new Part[]{act, quotePart});
        post.setEntity(entity);
        HttpResponse httpResponse = getHttpClient().execute(post);
        String result = readFromStream(httpResponse.getEntity().getContent());
        RulezResult rulezResult = new RulezResult();
        rulezResult.isOk = "Ok".equalsIgnoreCase(result);
        rulezResult.id = id;
        rulezResult.type = RulezType.getType(type);
        return rulezResult;
    }

    public AndroidHttpClient getHttpClient() {
        if (getContext() == null) {
            return null;
        }
        BashApplication app = (BashApplication) getContext().getApplicationContext();
        return app.getHttpClient();
    }

    public static class RulezResult {
        public String id;
        public RulezType type;
        public boolean isOk;
    }
}
