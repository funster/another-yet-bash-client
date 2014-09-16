package ru.aim.anotheryetbashclient.loaders;

import android.content.Context;
import android.os.Bundle;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import ru.aim.anotheryetbashclient.ActionsAndIntents;

import static ru.aim.anotheryetbashclient.helper.actions.Package.wrapWithRoot;

public class RatingLoader extends QuoteLoader {

    static final String ROOT_PAGE = wrapWithRoot("byrating");
    static final String NEXT_PAGE = wrapWithRoot("byrating/%s");

    int mCurrentPage = 1;
    int mMaxPage = -1;

    public RatingLoader(Context context, Bundle bundle) {
        super(context);
        mCurrentPage = bundle.getInt(ActionsAndIntents.CURRENT_PAGE, 1);
    }

    @Override
    protected void beforeParsing(Document document) {
        super.beforeParsing(document);
        if (isFirstPage()) {
            Element input = document.select("form[action=/byrating]").get(0).
                    select("span[class=current]").select("input[class=page]").get(0);
            mMaxPage = Integer.parseInt(input.attr("max"));
            if (mMaxPage != -1) {
                setTag(new RatingResult(mCurrentPage, mMaxPage));
            }
        }
    }

    boolean isFirstPage() {
        return mCurrentPage == 1;
    }

    @Override
    protected String getUrl() {
        if (isFirstPage()) {
            return ROOT_PAGE;
        } else {
            return String.format(NEXT_PAGE, mCurrentPage);
        }
    }

    public static class RatingResult {

        public RatingResult(int currentPage, int maxPage) {
            this.currentPage = currentPage;
            this.maxPage = maxPage;
        }

        public int currentPage;
        public int maxPage;
    }
}
