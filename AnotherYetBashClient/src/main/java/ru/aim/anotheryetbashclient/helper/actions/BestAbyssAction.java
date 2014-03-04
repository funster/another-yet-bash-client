package ru.aim.anotheryetbashclient.helper.actions;

import static ru.aim.anotheryetbashclient.helper.actions.Package.wrapWithRoot;

/**
 *
 */
public class BestAbyssAction extends AbstractAbyssAction {

    public static final String URL = wrapWithRoot("abyssbest");

    @Override
    protected String getUrl() {
        return URL;
    }
}
