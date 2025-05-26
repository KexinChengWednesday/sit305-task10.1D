package com.example.task61d;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class InterestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest);

        Button btnAlgo = findViewById(R.id.btnAlgorithms);
        Button btnDS = findViewById(R.id.btnDataStructures);
        Button btnWeb = findViewById(R.id.btnWebDev);
        Button btnTesting = findViewById(R.id.btnTesting);

        btnAlgo.setOnClickListener(v -> goToDashboardWithTopic("algorithms"));
        btnDS.setOnClickListener(v -> goToDashboardWithTopic("data structures"));
        btnWeb.setOnClickListener(v -> goToDashboardWithTopic("web development"));
        btnTesting.setOnClickListener(v -> goToDashboardWithTopic("software testing"));

        Button nextBtn = findViewById(R.id.btnNext);
        nextBtn.setOnClickListener(v -> goToDashboardWithTopic("general")); // 默认
    }

    private void goToDashboardWithTopic(String topic) {
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.putExtra("selected_topic", topic);
        startActivity(intent);
    }
}
