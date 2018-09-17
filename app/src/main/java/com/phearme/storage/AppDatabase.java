package com.phearme.storage;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {
        RoomComic.class
}, version = 1)
abstract class AppDatabase extends RoomDatabase {
    public abstract RoomComicDao roomComicDao();
}
