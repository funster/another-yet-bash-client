package ru.aim.anotheryetbashclient.loaders;

/**
 *
 */
@SuppressWarnings("unused")
public class SimpleResult<Result> {

    private Throwable error;
    private Result result;
    private Object tag;

    public SimpleResult() {
    }

    public SimpleResult(Throwable error) {
        this.error = error;
    }

    public SimpleResult(Result result) {
        this.result = result;
    }

    public SimpleResult(Throwable error, Result result) {
        this(error);
        this.result = result;
    }

    public SimpleResult(Throwable error, Result result, Object tag) {
        this(error, result);
        this.tag = tag;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public boolean containsError() {
        return error != null;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }
}
