package com.phearme.storage;

public interface IStorageEventHandler<T> {
    void onEvent(T result);
}
