package com.example.task61d;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class HistoryActivity extends AppCompatActivity {

    private LinearLayout historyContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        historyContainer = findViewById(R.id.historyContainer);

        // 正确回答的题目
        addHistoryCard("1. What is polymorphism?", "Overloading methods", "Overloading methods", true);
        addHistoryCard("2. What is Docker used for?", "Containerization", "Containerization", true);
        addHistoryCard("3. Which Android component handles navigation?", "Activity", "Activity", true);
        addHistoryCard("4. What is RESTful API?", "HTTP interface", "HTTP interface", true);
        addHistoryCard("5. What is SQL JOIN used for?", "Combining tables", "Combining tables", true);
        addHistoryCard("6. What is CI/CD?", "Continuous Integration", "Continuous Integration", true);
        addHistoryCard("7. What is ViewModel in Android?", "Holds UI data", "Holds UI data", true);

        // 错误回答的题目
        addHistoryCard("8. What is Kubernetes?", "A database", "Container orchestrator", false);
        addHistoryCard("9. What is JSON?", "Programming language", "Data format", false);
        addHistoryCard("10. What is an Intent in Android?", "A layout file", "Message object", false);
    }

    private void addHistoryCard(String question, String yourAnswer, String correctAnswer, boolean isCorrect) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(32, 32, 32, 32);
        card.setBackgroundResource(R.drawable.blue_card);
        card.setElevation(8f);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 24, 0, 0);
        card.setLayoutParams(params);

        TextView questionText = new TextView(this);
        questionText.setText(question);
        questionText.setTextSize(18f);
        questionText.setTypeface(null, Typeface.BOLD);
        questionText.setTextColor(getColor(android.R.color.white));
        card.addView(questionText);

        if (isCorrect) {
            TextView correct = new TextView(this);
            correct.setText("✔ Your Answer: " + correctAnswer);
            correct.setTextColor(getColor(android.R.color.holo_green_light));
            correct.setPadding(0, 8, 0, 0);
            card.addView(correct);
        } else {
            TextView incorrect = new TextView(this);
            incorrect.setText("✘ Your Answer: " + yourAnswer);
            incorrect.setTextColor(getColor(android.R.color.holo_red_light));
            incorrect.setPadding(0, 8, 0, 0);
            card.addView(incorrect);

            TextView correct = new TextView(this);
            correct.setText("✔ Correct Answer: " + correctAnswer);
            correct.setTextColor(getColor(android.R.color.holo_green_light));
            correct.setPadding(0, 4, 0, 0);
            card.addView(correct);
        }

        historyContainer.addView(card);
    }
}
