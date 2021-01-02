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
                FirebaseModel firebaseModel = (FirebaseModel) snapshot.getValue();

                binding.potassium.setText(String.valueOf(firebaseModel.K));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ResultActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        };
        databaseReference.addValueEventListener(valueEventListener);
    }
}