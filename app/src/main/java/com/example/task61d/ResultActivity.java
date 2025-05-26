package com.example.task61d;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

public class ResultActivity extends AppCompatActivity {

    private LinearLayout resultContainer;
    private Button continueBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        resultContainer = findViewById(R.id.resultContainer);
        continueBtn = findViewById(R.id.btnContinue);

        try {
            String quizData = getIntent().getStringExtra("quiz_data");
            String answers = getIntent().getStringExtra("user_answers");

            JSONArray quizArray = new JSONArray(quizData);
            JSONArray answerArray = new JSONArray(answers);

            for (int i = 0; i < quizArray.length(); i++) {
                JSONObject qObj = quizArray.getJSONObject(i);
                String question = qObj.getString("question");
                String userAnswer = answerArray.getString(i);

                // 模拟模型分析文本
                String analysis = generateFeedback(question, userAnswer);

                TextView resultBlock = new TextView(this);
                resultBlock.setText((i + 1) + ". " + question + "\n\nYour Answer: " + userAnswer + "\n\nModel Feedback: " + analysis);
                resultBlock.setTextSize(16);
                resultBlock.setPadding(24, 24, 24, 24);
                resultBlock.setTextColor(getResources().getColor(android.R.color.white));
                resultBlock.setBackgroundResource(R.drawable.blue_card);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 20, 0, 0);
                resultBlock.setLayoutParams(params);

                resultContainer.addView(resultBlock);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        continueBtn.setOnClickListener(v -> {
            startActivity(new Intent(ResultActivity.this, DashboardActivity.class));
            finish();
        });
    }

    // 模拟模型反馈（可自行扩展 AI 调用）
    private String generateFeedback(String question, String userAnswer) {
        if (userAnswer.equalsIgnoreCase("No Answer")) {
            return "Try to answer next time to improve your understanding.";
        } else if (question.toLowerCase().contains("docker")) {
            return "Correct! Docker is used for lightweight containerization of applications.";
        } else if (question.toLowerCase().contains("polymorphism")) {
            return "Great! Polymorphism helps objects behave differently based on their class.";
        } else if (question.toLowerCase().contains("android")) {
            return "Activity is the right choice for managing user interfaces.";
        } else {
            return "Good try! Review this topic to strengthen your knowledge.";
        }
    }
}
