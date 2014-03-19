package ru.aim.anotheryetbashclient.helper.actions;

import android.content.Intent;
import org.jsoup.nodes.Document;
import ru.aim.anotheryetbashclient.ActionsAndIntents;

import static ru.aim.anotheryetbashclient.helper.actions.Package.findMore;
import static ru.aim.anotheryetbashclient.helper.actions.Package.wrapWithRoot;
import static ru.aim.anotheryetbashclient.helper.actions.Package.wrapWithRootWithoutSlash;

public class BashRandomAction extends AbstractAction {

    static final String URL = wrapWithRoot("random");
    static final String MORE_URL = wrapWithRootWithoutSlash("%s");

    String more;

    @Override
    protected String getUrl() {
        String url;
        if (getIntent().hasExtra(ActionsAndIntents.NEXT_PAGE)) {
            url = String.format(MORE_URL, getIntent().getStringExtra(ActionsAndIntents.NEXT_PAGE));
        } else {
            url = URL;
        }
        return url;
    }

    @Override
    protected void beforeParsing(Document document) {
        super.beforeParsing(document);
        more = findMore(document);
    }

    @Override
    protected void afterParsing(Intent intent) {
        super.afterParsing(intent);
        intent.putExtra(ActionsAndIntents.NEXT_PAGE, more);
    }
}
