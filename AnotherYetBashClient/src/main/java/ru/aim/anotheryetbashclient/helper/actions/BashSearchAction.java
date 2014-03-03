package ru.aim.anotheryetbashclient.helper.actions;

import ru.aim.anotheryetbashclient.ActionsAndIntents;

import static ru.aim.anotheryetbashclient.helper.Utils.encode;

/**
 *
 */
public class BashSearchAction extends AbstractAction {

    static final String URL = "http://bash.im/index?text=%s";

    String url;

    @Override
    protected String getUrl() {
        if (url == null) {
            String search = getIntent().getStringExtra(ActionsAndIntents.SEARCH_QUERY);
            assert search != null;
            url = String.format(URL, encode(search));
        }
        return url;
    }
}
