package com.example.task61d;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TaskDetailActivity extends AppCompatActivity {

    private LinearLayout quizContainer;
    private Button submitBtn;
    private ProgressBar progressBar;
    private TextView loadingText;

    private JSONArray quizArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        quizContainer = findViewById(R.id.quizContainer);
        submitBtn = findViewById(R.id.btnSubmit);
        progressBar = findViewById(R.id.progressBar);
        loadingText = findViewById(R.id.loadingText);

        quizContainer.setVisibility(View.GONE);
        submitBtn.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        loadingText.setVisibility(View.VISIBLE);
        loadingText.setText("🤖 Generating IT quiz questions...");

        new Handler().postDelayed(() -> {
            progressBar.setVisibility(View.GONE);
            loadingText.setVisibility(View.GONE);
            loadLocalITQuestions();
        }, 7000);

        submitBtn.setOnClickListener(v -> {
            ArrayList<String> selectedAnswers = new ArrayList<>();
            for (int i = 0; i < quizArray.length(); i++) {
                RadioGroup group = findViewById(100 + i);
                int checkedId = group.getCheckedRadioButtonId();
                if (checkedId != -1) {
                    RadioButton selected = findViewById(checkedId);
                    selectedAnswers.add(selected.getText().toString());
                } else {
                    selectedAnswers.add("No Answer");
                }
            }

            Intent intent = new Intent(TaskDetailActivity.this, ResultActivity.class);
            intent.putExtra("quiz_data", quizArray.toString());
            intent.putExtra("user_answers", new JSONArray(selectedAnswers).toString());
            startActivity(intent);
        });
    }

    private void loadLocalITQuestions() {
        try {
            quizArray = new JSONArray();

            JSONObject q1 = new JSONObject();
            q1.put("question", "What is polymorphism in OOP?");
            q1.put("options", new JSONArray().put("A feature to overload methods").put("Creating multiple classes").put("Using multiple languages"));

            JSONObject q2 = new JSONObject();
            q2.put("question", "Which Android component is used for UI navigation?");
            q2.put("options", new JSONArray().put("Activity").put("Service").put("ContentProvider"));

            JSONObject q3 = new JSONObject();
            q3.put("question", "What is Docker used for?");
            q3.put("options", new JSONArray().put("Containerization").put("Database hosting").put("Cloud storage"));

            quizArray.put(q1);
            quizArray.put(q2);
            quizArray.put(q3);

            for (int i = 0; i < quizArray.length(); i++) {
                JSONObject qObj = quizArray.getJSONObject(i);
                String question = qObj.getString("question");
                JSONArray options = qObj.getJSONArray("options");

                TextView qText = new TextView(this);
                qText.setText((i + 1) + ". " + question);
                qText.setTextSize(18);
                qText.setPadding(0, 20, 0, 8);
                quizContainer.addView(qText);

                RadioGroup group = new RadioGroup(this);
                group.setId(100 + i);
                for (int j = 0; j < options.length(); j++) {
                    RadioButton btn = new RadioButton(this);
                    btn.setText(options.getString(j));
                    group.addView(btn);
                }
                quizContainer.addView(group);
            }

            quizContainer.setVisibility(View.VISIBLE);
            submitBtn.setVisibility(View.VISIBLE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
