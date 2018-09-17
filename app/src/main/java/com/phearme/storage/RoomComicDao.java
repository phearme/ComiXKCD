package com.phearme.storage;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

@Dao
public interface RoomComicDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RoomComic roomComic);

    @Query("SELECT * FROM RoomComic WHERE num = :num")
    RoomComic getByNum(int num);
}
