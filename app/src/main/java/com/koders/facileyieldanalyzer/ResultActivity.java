package com.koders.facileyieldanalyzer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.koders.facileyieldanalyzer.databinding.ActivityResultBinding;

public class ResultActivity extends AppCompatActivity {

    ActivityResultBinding binding;
    DatabaseReference databaseReference;
    double potassium, moisture, nitrogen, phosphorus, pH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_result);
        databaseReference = FirebaseDatabase.getInstance().getReference("home/");

        binding.exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(ResultActivity.this, LoginActivity.class));
                FirebaseAuth.getInstance().signOut();
                finishAffinity();
//                System.exit(0);
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
        databaseReference.addValueEventListener(valueEventListener);
    }

    private String predictCrop(double moisture, double pH) {
        if (moisture == 30 && (pH >= 6.0 && pH <= 6.5)) {
            //sugar cane
            return "Sugar cane recommended";
        } else if ((moisture >= 65 && moisture <= 75) && (pH >= 6.0 && pH <= 6.8)) {
            //tomato
            return "Tomato recommended";
        } else if ((moisture >= 20 && moisture <= 22) && (pH >= 5 && pH <= 8)) {
            //rice
            return "Rice crop recommended";
        } else if ((moisture >= 80 && moisture <= 90) && (pH >= 5.3 && pH <= 6.0)) {
            //potato
            return "Potato crop recommended";
        } else {
            return "No crop recommended";
        }
    }
}