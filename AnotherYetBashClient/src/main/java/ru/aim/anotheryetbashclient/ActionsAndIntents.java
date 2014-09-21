package ru.aim.anotheryetbashclient;

@SuppressWarnings("unused")
public class ActionsAndIntents {

    /**
     * Actions in intents
     */
    //-----------------------------------------------------------------------

    public static final String REFRESH = "ru.aim.anotheryetbashclient.REFRESH";
    public static final String NOTIFY = "ru.aim.anotheryetbashclient.NOTIFY";

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
    public static final int TYPE_NEW = 0;
    public static final int TYPE_RANDOM = 1;
    public static final int TYPE_BEST = 2;
    public static final int TYPE_BY_RATING = 3;
    public static final int TYPE_ABYSS = 4;
    public static final int TYPE_TOP_ABYSS = 8;
    public static final int TYPE_BEST_ABYSS = 6;
    public static final int TYPE_OFFLINE = 7;
    public static final int TYPE_FAVORITES = 5;
    public static final int TYPE_RULEZ = 10;
    public static final int TYPE_SUX = 11;
    public static final int TYPE_SEARCH = 12;

    /**
     * Preferences
     */
    //-----------------------------------------------------------------------

    public static final String CURRENT_QUOTES = "current-quotes";

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
}
