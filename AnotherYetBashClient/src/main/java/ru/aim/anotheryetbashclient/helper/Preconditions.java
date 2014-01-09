package ru.aim.anotheryetbashclient.helper;

@SuppressWarnings("unused")
public final class Preconditions {

    private Preconditions() {
        throw new AssertionError();
    }

    public static void notNull(Object o) {
        if (o == null) {
            throw new NullPointerException("Should be not null");
        }
    }
}
