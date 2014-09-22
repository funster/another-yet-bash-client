package ru.aim.anotheryetbashclient.loaders;

public enum RulezType {

    RULEZ("rulez"), SUX("sux"), BAYAN("bayan");

    String name;

    RulezType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static RulezType getType(String name) {
        for (RulezType type : values()) {
            if (type.name.equals(name)) {
                return type;
            }
        }
        return null;
    }
}
