package com.example.task61d;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    private String username;
    private TextView greeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // 获取从登录页传入的用户名
        username = getIntent().getStringExtra("username");
        greeting = findViewById(R.id.tvGreeting);
        greeting.setText("Hello, " + (username != null ? username : "User"));

        // 绑定卡片并设置点击事件
        LinearLayout taskCard = findViewById(R.id.taskCard);
        LinearLayout profileCard = findViewById(R.id.profileCard);
        LinearLayout historyCard = findViewById(R.id.historyCard);
        LinearLayout upgradeCard = findViewById(R.id.upgradeCard);
        LinearLayout shareCard = findViewById(R.id.shareCard);

        // 跳转各个功能页面
        taskCard.setOnClickListener(v -> startActivity(new Intent(this, TaskDetailActivity.class)));

        profileCard.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra("username", username);
            intent.putExtra("email", username + "@example.com"); // 示例邮箱
            startActivity(intent);
        });

        historyCard.setOnClickListener(v -> {
            Intent intent = new Intent(this, HistoryActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        upgradeCard.setOnClickListener(v -> startActivity(new Intent(this, UpgradeActivity.class)));

        shareCard.setOnClickListener(v -> startActivity(new Intent(this, ShareProfileActivity.class)));
    }
}
