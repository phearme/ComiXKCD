package com.phearme.storage;

import android.arch.persistence.room.Room;
import android.content.Context;

public class Storage {
    private static Storage mInstance;
    private AppDatabase db;
    private StorageTaskFactory taskFactory;

    public static synchronized Storage getInstance(Context context) {
        if (mInstance == null) { mInstance = new Storage(context.getApplicationContext()); }
        return mInstance;
    }

    private Storage(Context context) {
        taskFactory = new StorageTaskFactory();
        db = Room.databaseBuilder(context, AppDatabase.class, "db-comixkcd").build();
    }

    public void getComic(final int num, IStorageEventHandler<RoomComic> callback) {
        taskFactory.buildTask(new ReturnRunnable<RoomComic>() {
            @Override
            public void run() {
                setResult(db.roomComicDao().getByNum(num));
            }
        }, callback).execute();
    }

    public void saveComic(final RoomComic roomComic, IStorageEventHandler callback) {
        taskFactory.buildTask(new ReturnRunnable() {
            @Override
            public void run() {
                db.roomComicDao().insert(roomComic);
            }
        }, callback).execute();
    }
}
