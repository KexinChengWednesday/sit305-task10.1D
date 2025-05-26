package com.example.task61d;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class ResultActivity extends AppCompatActivity {

    private LinearLayout resultContainer;
    private Button btnContinue;

    private String LLM_API_URL = "https://llm2chatbot-f3c8a49e3bb7.herokuapp.com/api/chat";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        resultContainer = findViewById(R.id.resultContainer);
        btnContinue = findViewById(R.id.btnContinue);

        String quizJson = getIntent().getStringExtra("quiz_data");
        String answerJson = getIntent().getStringExtra("user_answers");

        try {
            JSONArray quizArray = new JSONArray(quizJson);
            JSONArray answerArray = new JSONArray(answerJson);

            for (int i = 0; i < quizArray.length(); i++) {
                JSONObject questionObj = quizArray.getJSONObject(i);
                String question = questionObj.getString("question");
                String correctAnswer = questionObj.getString("answer");
                String userAnswer = answerArray.getString(i);

                int index = i + 1;
                boolean isCorrect = correctAnswer.equalsIgnoreCase(userAnswer);

                LinearLayout card = new LinearLayout(this);
                card.setOrientation(LinearLayout.VERTICAL);
                card.setPadding(24, 24, 24, 24);
                card.setBackgroundResource(R.drawable.result_card_background);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                lp.setMargins(0, 0, 0, 32);
                card.setLayoutParams(lp);

                TextView qText = new TextView(this);
                qText.setText(index + ". " + question);
                qText.setTextSize(16);
                qText.setTextColor(getResources().getColor(android.R.color.white));

                TextView aText = new TextView(this);
                aText.setText("Your Answer: " + userAnswer);
                aText.setTextColor(getResources().getColor(android.R.color.white));

                TextView resultText = new TextView(this);
                resultText.setText(isCorrect ? "✅ Correct" : "❌ Incorrect");
                resultText.setTextColor(isCorrect ? getResources().getColor(android.R.color.holo_green_light) :
                        getResources().getColor(android.R.color.holo_red_light));

                card.addView(qText);
                card.addView(aText);
                card.addView(resultText);

                resultContainer.addView(card);

                // generateLLMFeedback(question, userAnswer, correctAnswer); // optional
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        btnContinue.setOnClickListener(v -> startActivity(new Intent(this, DashboardActivity.class)));
    }

    // 示例：调用真实 LLM 模型（使用 llm2chatbot）
    private void generateLLMFeedback(String question, String userAnswer, String correctAnswer) {
        new Thread(() -> {
            try {
                URL url = new URL(LLM_API_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                JSONObject payload = new JSONObject();
                payload.put("message", "The question was: " + question + "\nMy answer: " + userAnswer +
                        "\nCorrect answer: " + correctAnswer +
                        "\nPlease provide a short feedback on my answer.");

                OutputStream os = conn.getOutputStream();
                os.write(payload.toString().getBytes());
                os.flush();

                Scanner in = new Scanner(conn.getInputStream());
                StringBuilder sb = new StringBuilder();
                while (in.hasNext()) {
                    sb.append(in.nextLine());
                }
                in.close();

                JSONObject response = new JSONObject(sb.toString());
                String reply = response.getString("reply");

                runOnUiThread(() -> Toast.makeText(this, reply, Toast.LENGTH_LONG).show());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
