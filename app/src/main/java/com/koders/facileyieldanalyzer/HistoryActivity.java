package com.koders.facileyieldanalyzer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    Button back, clearAll;
    RecyclerView listView;
    HistoryAdapter historyAdapter;
    List<FirebaseModel> list = new ArrayList<>();

    CropDao cropDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        cropDao = CropDatabase.getInstance(this).firebaseDao();
        back = findViewById(R.id.back);
        clearAll = findViewById(R.id.clearAll);
        listView = findViewById(R.id.listView);
        list.addAll(cropDao.getAllCrops());
        historyAdapter = new HistoryAdapter(list);

        listView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setAdapter(historyAdapter);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        clearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cropDao.deleteAll();
                list.clear();
                historyAdapter.notifyDataSetChanged();
            }
        });
    }
}