package com.koders.facileyieldanalyzer;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {FirebaseModel.class}, version = 1)
public abstract class CropDatabase extends RoomDatabase {

    public abstract CropDao firebaseDao();

    private static CropDatabase firebaseDB;

    public static CropDatabase getInstance(Context context) {
        if (null == firebaseDB) {
            firebaseDB = buildDatabaseInstance(context);
        }
        return firebaseDB;
    }

    private static CropDatabase buildDatabaseInstance(Context context) {
        return Room.databaseBuilder(context,
                CropDatabase.class,
                "firebase_database")
                .allowMainThreadQueries()
                .build();
    }

}
