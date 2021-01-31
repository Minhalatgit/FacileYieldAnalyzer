package com.koders.facileyieldanalyzer;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Calendar;
import java.util.Date;

@Entity(tableName = "crop_table")
public class FirebaseModel {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public long createdAt = System.currentTimeMillis();
    public double K;
    public String Moisture;
    public double N;
    public double P;
    public String PH;

    public FirebaseModel() {
    }

    public FirebaseModel(double k, String moisture, double n, double p, String PH) {
        K = k;
        Moisture = moisture;
        N = n;
        P = p;
        this.PH = PH;
    }
}
