package ru.aim.anotheryetbashclient.helper.actions;

@SuppressWarnings("unused")
public class BashBestAction extends AbstractAction {

    public static final String TAG = "BashBestAction";

    static final String URL = "http://bash.im/best";

    @Override
    protected String getUrl() {
        return URL;
    }
}
