package com.phearme.storage;

public abstract class ReturnRunnable implements Runnable {
    private Object result;

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
