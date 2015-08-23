package ru.aim.anotheryetbashclient;

public enum QType {

    FRESH("FRESH", 0),
    RANDOM("RANDOM", 1),
    BEST("BEST", 2),
    BY_RATING("BY_RATING", 3),
    ABYSS("ABYSS", 4),
    TOP_ABYSS("TOP_ABYSS", 5),
    BEST_ABYSS("BEST_ABYSS", 6);

    final String stringId;
    final int typeId;

    QType(String id, int typeId) {
        this.stringId = id;
        this.typeId = typeId;
    }

    public String getTableName() {
        return "quotes_" + stringId.toLowerCase();
    }
}
