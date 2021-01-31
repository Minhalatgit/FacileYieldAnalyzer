package com.koders.facileyieldanalyzer;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CropDao {

    @Insert
    void insertCropData(FirebaseModel firebaseModel);

    @Query("DELETE FROM crop_table")
    void deleteAll();

    @Query("SELECT * FROM crop_table")
    List<FirebaseModel> getAllCrops();
}
