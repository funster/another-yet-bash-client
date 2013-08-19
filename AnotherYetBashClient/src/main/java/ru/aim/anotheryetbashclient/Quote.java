package ru.aim.anotheryetbashclient;

import java.io.Serializable;

public class Quote implements Serializable {

    public final String date;
    public final String id;
    public final String text;

    public Quote(String date, String id, String text) {
        this.date = date;
        this.id = id;
        this.text = text;
    }

    public static class Builder {
        public String date;
        public String id;
        public String text;

        public Builder date(String date) {
            this.date = date;
            return this;
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Quote build() {
            return new Quote(date, id, text);
        }
    }
}
