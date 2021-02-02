package com.koders.facileyieldanalyzer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.koders.facileyieldanalyzer.databinding.ActivityResultBinding;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ResultActivity extends AppCompatActivity {

    ActivityResultBinding binding;
    DatabaseReference databaseReference;
    double potassium, moisture, nitrogen, phosphorus, pH;
    CropDatabase cropDatabase;
    int smallestPos;
    boolean isMoistureGreater = false;
    boolean isPhGreater = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_result);
        databaseReference = FirebaseDatabase.getInstance().getReference("home/");
        cropDatabase = CropDatabase.getInstance(this);

        binding.exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finishAffinity();
            }
        });

        binding.reScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResultActivity.this, ExamineActivity.class));
                finishAffinity();
            }
        });


        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                binding.progress.setVisibility(View.GONE);
                FirebaseModel firebaseModel = snapshot.getValue(FirebaseModel.class);
                cropDatabase.firebaseDao().insertCropData(firebaseModel);

                binding.nitrogen.setText(String.valueOf(firebaseModel.N));
                binding.potassium.setText(String.valueOf(firebaseModel.K));
                binding.phosphorus.setText(String.valueOf(firebaseModel.P));
                binding.moisture.setText(String.valueOf(firebaseModel.Moisture));
                binding.ph.setText(String.valueOf(firebaseModel.PH));

                moisture = Double.parseDouble(firebaseModel.Moisture);
                pH = Double.parseDouble(firebaseModel.PH);

                binding.recommendedCrop.setText(predictCrop(moisture, pH));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ResultActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        };
        binding.progress.setVisibility(View.VISIBLE);
        databaseReference.addListenerForSingleValueEvent(valueEventListener);
    }

    private String predictCrop(double moisture, double pH) {
        if ((moisture >= 20 && moisture <= 22) && (pH >= 5 && pH <= 8)) {
            //rice
            return "Rice crop recommended";
        } else if ((moisture >= 65 && moisture <= 75) && (pH >= 6.0 && pH <= 6.8)) {
            //tomato
            return "Tomato recommended";
        } else if (moisture == 30 && (pH >= 6.0 && pH <= 6.5)) {
            //sugar cane
            return "Sugar cane recommended";
        } else if ((moisture >= 80 && moisture <= 90) && (pH >= 5.3 && pH <= 6.0)) {
            //potato
            return "Potato crop recommended";
        } else {
            /*
             * 0 -> rice
             * 1 -> tomato
             * 2 -> sugar
             * 3 -> potato
             */
            double[] moistureDifferences = new double[4];
            double[] pHDifferences = new double[4];

            //for rice
            double[] riceMoistureRange = {20, 22};
            double[] ricePhRange = {5, 8};

            if (moisture < riceMoistureRange[0]) {
                isMoistureGreater = false;
                moistureDifferences[0] = riceMoistureRange[0] - moisture;
            } else {
                isMoistureGreater = true;
                moistureDifferences[0] = moisture - riceMoistureRange[0];
            }

            if (pH < ricePhRange[0]) {
                pHDifferences[0] = ricePhRange[0] - pH;
            } else {
                pHDifferences[0] = pH - ricePhRange[0];
            }

            //for tomato
            double[] tomatoMoistureRange = {65, 75};
            double[] tomatoPhRange = {6.0, 6.8};

            if (moisture < tomatoMoistureRange[0]) {
                isMoistureGreater = false;
                moistureDifferences[1] = tomatoMoistureRange[0] - moisture;
            } else {
                isMoistureGreater = true;
                moistureDifferences[1] = moisture - tomatoMoistureRange[0];
            }

            if (pH < tomatoPhRange[0]) {
                pHDifferences[0] = tomatoPhRange[0] - pH;
            } else {
                pHDifferences[0] = pH - tomatoPhRange[0];
            }

            //for sugar cane
            double sugarMoistureRange = 30;
            double[] sugarPhRange = {6.0, 6.5};

            if (moisture < sugarMoistureRange) {
                isMoistureGreater = false;
                moistureDifferences[2] = sugarMoistureRange - moisture;
            } else {
                isMoistureGreater = true;
                moistureDifferences[2] = moisture - sugarMoistureRange;
            }

            if (pH < sugarPhRange[0]) {
                pHDifferences[2] = sugarPhRange[0] - pH;
            } else {
                pHDifferences[2] = pH - sugarPhRange[0];
            }

            //for potato
            double[] potatoMoistureRange = {80, 90};
            double[] potatoPhRange = {5.3, 6.0};

            if (moisture < potatoMoistureRange[0]) {
                isMoistureGreater = false;
                moistureDifferences[3] = potatoMoistureRange[0] - moisture;
            } else {
                isMoistureGreater = true;
                moistureDifferences[3] = moisture - potatoMoistureRange[0];
            }

            if (pH < potatoPhRange[0]) {
                pHDifferences[2] = potatoPhRange[0] - pH;
            } else {
                pHDifferences[2] = pH - potatoPhRange[0];
            }

            for (double d : moistureDifferences) {
                Log.d("moisture differences", "" + d);
            }
            for (double d : pHDifferences) {
                Log.d("ph differences", "" + d);
            }
            Log.d("small difference", getMinValue(moistureDifferences) + " " + smallestPos);

            double smallestMoisture = getMinValue(moistureDifferences);

            /*
             * 0 -> rice
             * 1 -> tomato
             * 2 -> sugar
             * 3 -> potato
             */

            switch (smallestPos) {
                case 0:
                    if (isMoistureGreater)
                        return "Best recommended crop is rice, you need to decrease " + smallestMoisture + " moisture";
                    else
                        return "Best recommended crop is rice, you need to increase " + smallestMoisture + " moisture";
                case 1:
                    if (isMoistureGreater)
                        return "Best recommended crop is tomato, you need to decrease " + smallestMoisture + " moisture";
                    else
                        return "Best recommended crop is tomato, you need to increase " + smallestMoisture + " moisture";
                case 2:
                    if (isMoistureGreater)
                        return "Best recommended crop is sugar, you need to decrease " + smallestMoisture + " moisture";
                    else
                        return "Best recommended crop is sugar, you need to increase " + smallestMoisture + " moisture";
                case 3:
                    if (isMoistureGreater)
                        return "Best recommended crop is potato, you need to decrease " + smallestMoisture + " moisture";
                    else
                        return "Best recommended crop is potato, you need to increase " + smallestMoisture + " moisture";
                default:
                    return "No crop recommended";
            }
        }
    }

    public double getMinValue(double[] array) {
        double minValue = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < minValue) {
                minValue = array[i];
                smallestPos = i;
            }
        }
        return minValue;
    }

}