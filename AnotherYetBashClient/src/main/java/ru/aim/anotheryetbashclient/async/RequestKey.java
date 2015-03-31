package ru.aim.anotheryetbashclient.async;

class RequestKey {

    final int commandKey;
    final int sourceKey;

    RequestKey(int commandKey, int sourceKey) {
        this.commandKey = commandKey;
        this.sourceKey = sourceKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequestKey that = (RequestKey) o;

        if (commandKey != that.commandKey) return false;
        if (sourceKey != that.sourceKey) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = commandKey;
        result = 31 * result + sourceKey;
        return result;
    }

    @Override
    public String toString() {
        return "RequestKey{" +
                "commandKey=" + commandKey +
                ", sourceKey=" + sourceKey +
                '}';
    }
}
