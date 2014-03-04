package ru.aim.anotheryetbashclient.helper.actions;

import android.content.ContentValues;
import android.content.Intent;
import org.jsoup.nodes.Document;
import ru.aim.anotheryetbashclient.ActionsAndIntents;

/**
 *
 */
abstract class AbstractAbyssAction extends AbstractAction {

    @Override
    protected void beforeParsing(Document document) {
        getDbHelper().clearAbyss();
    }

    @Override
    protected void saveQuote(ContentValues values) {
        getDbHelper().addNewQuoteAbyss(values);
    }

    @Override
    protected void afterParsing(Intent intent) {
        intent.putExtra(ActionsAndIntents.ABYSS, true);
    }
}
