package ru.aim.anotheryetbashclient.helper.actions;

import static ru.aim.anotheryetbashclient.helper.actions.Package.wrapWithRoot;

/**
 *
 */
public class TopAbyssAction extends AbstractAbyssAction {

    static final String URL = wrapWithRoot("abysstop");

    @Override
    protected String getUrl() {
        return URL;
    }
}
