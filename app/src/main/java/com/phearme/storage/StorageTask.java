package com.phearme.storage;

import android.os.AsyncTask;

public class StorageTask extends AsyncTask<Void, Void, Object> {
    private IStorageEventHandler callback;
    private ReturnRunnable runnable;

    StorageTask(ReturnRunnable runnable, IStorageEventHandler callback) {
        this.callback = callback;
        this.runnable = runnable;
    }

    @Override
    protected Object doInBackground(Void... voids) {
        runnable.run();
        return runnable.getResult();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onPostExecute(Object o) {
        if (callback != null) {
            callback.onEvent(o);
        }
    }

    public ReturnRunnable getRunnable() {
        return runnable;
    }

    public IStorageEventHandler getCallback() {
        return callback;
    }

}

