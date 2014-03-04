package ru.aim.anotheryetbashclient.helper.actions;

import android.content.ContentValues;
import android.content.Intent;
import org.jsoup.nodes.Document;
import ru.aim.anotheryetbashclient.ActionsAndIntents;

import static java.lang.String.format;
import static ru.aim.anotheryetbashclient.helper.actions.Package.findMore;
import static ru.aim.anotheryetbashclient.helper.actions.Package.wrapWithRoot;

public class AbyssAction extends AbstractAbyssAction {

    static final String URL = wrapWithRoot("abyss");
    static final String MORE_URL = wrapWithRoot("abyss%s");

    String url;
    String next;

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

    @Override
    protected void beforeParsing(Document document) {
        super.beforeParsing(document);
        next = findMore(document);
    }

    @Override
    protected void saveQuote(ContentValues values) {
        super.saveQuote(values);
        getDbHelper().addNewQuoteAbyss(values);
    }

    @Override
    protected void afterParsing(Intent intent) {
        super.afterParsing(intent);
        intent.putExtra(ActionsAndIntents.NEXT_PAGE, next);
    }
}
