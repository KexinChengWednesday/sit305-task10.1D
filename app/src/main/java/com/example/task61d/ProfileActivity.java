package com.example.task61d;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvUsername, tvEmail, tvTotal, tvCorrect, tvIncorrect, tvSummary;
    private Button btnShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvUsername = findViewById(R.id.tvUsername);
        tvEmail = findViewById(R.id.tvEmail);
        tvTotal = findViewById(R.id.tvTotalQuestions);
        tvCorrect = findViewById(R.id.tvCorrectAnswers);
        tvIncorrect = findViewById(R.id.tvIncorrectAnswers);
        tvSummary = findViewById(R.id.tvSummary);
        btnShare = findViewById(R.id.btnShare);

        // 模拟从登录获取用户名
        String username = getIntent().getStringExtra("username");
        tvUsername.setText(username);
        tvEmail.setText(username + "@example.com");

        // 模拟数据（10题中7题正确）
        int total = 10;
        int correct = 7;
        int incorrect = 3;

        tvTotal.setText(String.valueOf(total));
        tvCorrect.setText(String.valueOf(correct));
        tvIncorrect.setText(String.valueOf(incorrect));

        // 模拟3道错题（在真实情况下应从 ResultActivity 或数据库中获取）
        JSONArray errorArray = new JSONArray();
        try {
            JSONObject q1 = new JSONObject();
            q1.put("question", "What is Kubernetes?");
            q1.put("yourAnswer", "A database");
            q1.put("correctAnswer", "A container orchestration platform");

            JSONObject q2 = new JSONObject();
            q2.put("question", "What is JSON?");
            q2.put("yourAnswer", "A programming language");
            q2.put("correctAnswer", "A lightweight data-interchange format");

            JSONObject q3 = new JSONObject();
            q3.put("question", "What is an Intent in Android?");
            q3.put("yourAnswer", "A layout file");
            q3.put("correctAnswer", "A messaging object for component communication");

            errorArray.put(q1);
            errorArray.put(q2);
            errorArray.put(q3);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 调用真实 LLM 模型生成总结
        generateLLMSummary(errorArray);
    }

    private void generateLLMSummary(JSONArray errorQuestions) {
        new Thread(() -> {
            try {
                URL url = new URL("http://10.0.2.2:5000/summarizeErrors");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                JSONObject payload = new JSONObject();
                payload.put("errors", errorQuestions);

                OutputStream os = conn.getOutputStream();
                OutputStreamWriter writer = new OutputStreamWriter(os, "UTF-8");
                writer.write(payload.toString());
                writer.flush();
                writer.close();
                os.close();

                Scanner scanner = new Scanner(conn.getInputStream());
                StringBuilder response = new StringBuilder();
                while (scanner.hasNext()) {
                    response.append(scanner.nextLine());
                }
                scanner.close();

                JSONObject result = new JSONObject(response.toString());
                String summary = result.getString("summary");

                runOnUiThread(() -> tvSummary.setText("🧠 AI Summary:\n" + summary));

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> tvSummary.setText("❌ Failed to generate AI summary."));
            }
        }).start();
    }
}
