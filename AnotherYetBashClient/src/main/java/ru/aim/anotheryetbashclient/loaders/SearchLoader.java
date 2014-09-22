package ru.aim.anotheryetbashclient.loaders;

import android.content.Context;
import android.os.Bundle;

import static ru.aim.anotheryetbashclient.helper.Utils.encode;
import static ru.aim.anotheryetbashclient.loaders.Package.wrapWithRoot;

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
