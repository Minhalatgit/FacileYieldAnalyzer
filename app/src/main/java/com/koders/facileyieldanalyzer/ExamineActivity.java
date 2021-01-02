package com.koders.facileyieldanalyzer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class ExamineActivity extends AppCompatActivity {

    ImageView examine, hamburger;
    Button history, exit;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examine);

        init();

        examine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ExamineActivity.this, ResultActivity.class));
            }
        });

        hamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ExamineActivity.this, HistoryActivity.class));
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ExamineActivity.this, LoginActivity.class));
                drawerLayout.closeDrawer(GravityCompat.START);
                FirebaseAuth.getInstance().signOut();
                finishAffinity();
//                System.exit(0);
            }
        });

    }

    private void init() {
        NavigationView navigationView = findViewById(R.id.navView);
        examine = findViewById(R.id.examine);
        hamburger = findViewById(R.id.hamburger);
        history = navigationView.findViewById(R.id.history);
        exit = navigationView.findViewById(R.id.exit);

        drawerLayout = findViewById(R.id.drawer);

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}