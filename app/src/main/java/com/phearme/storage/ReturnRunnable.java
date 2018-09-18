package com.phearme.storage;

public abstract class ReturnRunnable<T> implements Runnable {
    private T result;

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
