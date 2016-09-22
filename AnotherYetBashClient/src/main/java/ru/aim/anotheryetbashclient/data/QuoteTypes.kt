package ru.aim.anotheryetbashclient.data

@Deprecated("should be removed after Quote sealed classes will be completed")
enum class QuoteTypes {
    TYPE_NEW,
    TYPE_RANDOM,
    TYPE_BEST,
    TYPE_BY_RATING,
    TYPE_ABYSS,
    TYPE_TOP_ABYSS,
    TYPE_BEST_ABYSS,
    TYPE_FAVORITES,
    TYPE_LIKE,
    TYPE_DISLIKE,
    TYPE_SEARCH;
}