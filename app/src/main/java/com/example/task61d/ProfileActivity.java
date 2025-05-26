package com.example.task61d;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvUsername, tvEmail, tvTotal, tvCorrect, tvIncorrect;
    private Button btnShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);  // 注意：使用美化版 activity_profile_pretty.xml 时请改为对应名称

        // 绑定视图
        tvUsername = findViewById(R.id.tvUsername);
        tvEmail = findViewById(R.id.tvEmail);
        tvTotal = findViewById(R.id.tvTotal);
        tvCorrect = findViewById(R.id.tvCorrect);
        tvIncorrect = findViewById(R.id.tvIncorrect);
        btnShare = findViewById(R.id.btnShare);

        // 从 Intent 获取用户信息
        String username = getIntent().getStringExtra("username");
        String email = getIntent().getStringExtra("email");

        // 示例：统计信息（后续可替换为真实数据）
        int totalQuestions = 10;
        int correctAnswers = 7;
        int incorrectAnswers = totalQuestions - correctAnswers;

        // 设置文本
        tvUsername.setText(username != null ? username : "Username");
        tvEmail.setText(email != null ? email : "user@email.com");
        tvTotal.setText(String.valueOf(totalQuestions));
        tvCorrect.setText(String.valueOf(correctAnswers));
        tvIncorrect.setText(String.valueOf(incorrectAnswers));

        // 分享按钮逻辑（示例）
        btnShare.setOnClickListener(v -> {
            String shareText = "👤 " + tvUsername.getText().toString() +
                    "\n📧 " + tvEmail.getText().toString() +
                    "\n✅ Correct: " + tvCorrect.getText().toString() +
                    "\n❌ Incorrect: " + tvIncorrect.getText().toString();

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, shareText);
            startActivity(Intent.createChooser(intent, "Share Profile Via"));
        });
    }
}
