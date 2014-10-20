package ru.pavelshackih.anotheryetbashclient.loaders;

import android.content.Context;
import android.os.Bundle;

import static ru.pavelshackih.anotheryetbashclient.helper.Utils.encode;
import static ru.pavelshackih.anotheryetbashclient.loaders.Package.wrapWithRoot;

public class SearchLoader extends QuoteLoader {

    static final String URL = wrapWithRoot("index?text=%s");

    String search;

    public SearchLoader(Context context, Bundle bundle) {
        super(context);
        search = bundle.getString("search");
    }

    @Override
    protected String getUrl() {
        return String.format(URL, encode(search));
    }
}
