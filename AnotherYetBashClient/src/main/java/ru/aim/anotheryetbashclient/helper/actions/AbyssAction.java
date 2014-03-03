package ru.aim.anotheryetbashclient.helper.actions;

import ru.aim.anotheryetbashclient.ActionsAndIntents;

import static java.lang.String.format;

public class AbyssAction extends AbstractAction {

    static final String URL = "http://bash.im/abyss";
    static final String MORE_URL = "http://bash.im/abyss%s";

    String url;

    @Override
    protected String getUrl() {
        if (url == null) {
            if (getIntent().hasExtra(ActionsAndIntents.NEXT_PAGE)) {
                url = format(MORE_URL, getIntent().getStringExtra(ActionsAndIntents.NEXT_PAGE));
            } else {
                url = URL;
            }
        }
        return url;
    }
}
