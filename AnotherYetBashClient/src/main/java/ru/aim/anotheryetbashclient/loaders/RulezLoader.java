package ru.aim.anotheryetbashclient.loaders;

import android.content.Context;
import android.os.Bundle;

import static ru.aim.anotheryetbashclient.loaders.Package.wrapWithRoot;

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
//        HttpPost post = new HttpPost(String.format(REQUEST, id, type));
//        StringPart quotePart = new StringPart("quote", id, UTF_8);
//        StringPart act = new StringPart("act", type, UTF_8);
//        MultipartEntity entity = new MultipartEntity(new Part[]{act, quotePart});
//        post.setEntity(entity);
//        HttpResponse httpResponse = getHttpClient().execute(post);
//        String result = readFromStream(httpResponse.getEntity().getContent());
//        RulezResult rulezResult = new RulezResult();
//        rulezResult.isOk = "Ok".equalsIgnoreCase(result);
//        rulezResult.id = id;
//        rulezResult.type = RulezType.getType(type);
        return null;
    }

    public static class RulezResult {
        public String id;
        public RulezType type;
        public boolean isOk;
    }
}
