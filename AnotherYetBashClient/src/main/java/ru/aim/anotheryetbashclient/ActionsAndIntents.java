package ru.aim.anotheryetbashclient;

@SuppressWarnings("unused")
public class ActionsAndIntents {

    /**
     * Actions in intents
     */
    //-----------------------------------------------------------------------

    public static final String REFRESH = BuildConfig.APPLICATION_ID + ".REFRESH";
    public static final String NOTIFY = BuildConfig.APPLICATION_ID + ".NOTIFY";

    //-----------------------------------------------------------------------


    /**
     * Keys in intents
     */
    public static final String TYPE_ID = "TYPE_ID";
    public static final String QUOTE_ID = "QUOTE_ID";
    public static final String MESSAGE = "MESSAGE";
    public static final String IDS = "IDS";
    public static final String CURRENT_PAGE = "CURRENT_PAGE";
    public static final String SEARCH_QUERY = "SEARCH_QUERY";
    public static final String NEXT_PAGE = "NEXT_PAGE";
    public static final String ABYSS = "ABYSS";
    public static final String DATE = "DATE";
    public static final String MAX_PAGE = "MAX_PAGE";


    /**
     * Request types
     */
    public static final int TYPE_NEW = R.id.nav_fresh;
    public static final int TYPE_RANDOM = R.id.nav_random;
    public static final int TYPE_BEST = R.id.nav_best;
    public static final int TYPE_BY_RATING = R.id.nav_by_rating;
    public static final int TYPE_ABYSS = R.id.nav_abyss;
    public static final int TYPE_TOP_ABYSS = R.id.nav_top_abyss;
    public static final int TYPE_BEST_ABYSS = R.id.nav_best_abyss;
    public static final int TYPE_FAVORITES = R.id.nav_fav;
    public static final int TYPE_RULEZ = 13;
    public static final int TYPE_SUX = 12;
    public static final int TYPE_SEARCH = R.id.nav_search;

    /**
     * Preferences
     */
    //-----------------------------------------------------------------------

    public static final String CURRENT_QUOTES = "current-quotes";
    public static final int TYPE_OFFLINE = 13;

    //-----------------------------------------------------------------------

    public enum Types {
        TYPE_NEW(0),
        TYPE_RANDOM(1),
        TYPE_BEST(2),
        TYPE_BY_RATING(3),
        TYPE_ABYSS(4),
        TYPE_TOP_ABYSS(5),
        TYPE_BEST_ABYSS(6),
        TYPE_OFFLINE(7),
        TYPE_FAVORITES(8),
        TYPE_RULEZ(10),
        TYPE_SUX(11),
        TYPE_SEARCH(12);

        final int typeId;

        Types(int typeId) {
            this.typeId = typeId;
        }
    }

    // -----------------------------------------------------------------------
    // misc

    public static final String IS_FIRST_LAUNCH = "isFirst";
}
