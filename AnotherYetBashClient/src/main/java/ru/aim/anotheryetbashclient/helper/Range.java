package ru.aim.anotheryetbashclient.helper;

import java.io.Serializable;

/**
 *
 */
public class Range implements Serializable {

    String from;
    String to;

    public Range() {
    }

    public Range(String from, String to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "Range{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                '}';
    }
}
