package com.koders.facileyieldanalyzer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class ResultActivity extends AppCompatActivity {

    Button reScan, exit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        reScan = findViewById(R.id.reScan);
        exit = findViewById(R.id.exit);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(ResultActivity.this, LoginActivity.class));
                FirebaseAuth.getInstance().signOut();
                finishAffinity();
//                System.exit(0);
            }
        });

        reScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResultActivity.this, ExamineActivity.class));
                finishAffinity();
            }
        });
    }
}