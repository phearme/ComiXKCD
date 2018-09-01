package com.phearme.appmediator;

public interface IMediatorEventHandler<T> {
    void onEvent(T result);
}
