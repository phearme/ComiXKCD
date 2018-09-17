package com.phearme.storage;

public class StorageTaskFactory {
    public StorageTask buildTask(ReturnRunnable runnable, IStorageEventHandler callback) {
        return new StorageTask(runnable, callback);
    }
}
