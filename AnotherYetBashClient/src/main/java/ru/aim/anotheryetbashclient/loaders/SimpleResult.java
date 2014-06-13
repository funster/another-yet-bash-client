package ru.aim.anotheryetbashclient.loaders;

/**
 *
 */
public class SimpleResult<Result> {

    private Throwable error;
    private Result result;

    public SimpleResult() {
    }

    public SimpleResult(Throwable error) {
        this.error = error;
    }

    public SimpleResult(Result result) {
        this.result = result;
    }

    public SimpleResult(Throwable error, Result result) {
        this.error = error;
        this.result = result;
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
}
