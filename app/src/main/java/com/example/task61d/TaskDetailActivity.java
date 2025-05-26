package com.example.task61d;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.view.View;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class TaskDetailActivity extends AppCompatActivity {

    private LinearLayout quizContainer;
    private ProgressBar progressBar;
    private Button btnSubmit;
    private JSONArray quizArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        quizContainer = findViewById(R.id.quizContainer);
        progressBar = findViewById(R.id.progressBar);
        btnSubmit = findViewById(R.id.btnSubmit);

        quizContainer.setVisibility(View.GONE);
        btnSubmit.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        new Handler().postDelayed(() -> {
            quizArray = generateLocalQuiz();
            renderQuiz();
        }, 7000);

        btnSubmit.setOnClickListener(v -> {
            ArrayList<String> answers = new ArrayList<>();
            for (int i = 0; i < quizArray.length(); i++) {
                RadioGroup group = findViewById(100 + i);
                int checkedId = group.getCheckedRadioButtonId();
                if (checkedId != -1) {
                    RadioButton selected = findViewById(checkedId);
                    answers.add(selected.getText().toString());
                } else {
                    answers.add("No Answer");
                }
            }
            Intent intent = new Intent(TaskDetailActivity.this, ResultActivity.class);
            intent.putExtra("quiz_data", quizArray.toString());
            intent.putExtra("user_answers", new JSONArray(answers).toString());
            startActivity(intent);
        });
    }

    private JSONArray generateLocalQuiz() {
        JSONArray array = new JSONArray();
        try {
            JSONObject q1 = new JSONObject();
            q1.put("question", "What is polymorphism in OOP?");
            q1.put("options", new JSONArray().put("Multiple forms").put("Single inheritance").put("Encapsulation"));
            q1.put("answer", "Multiple forms");

            JSONObject q2 = new JSONObject();
            q2.put("question", "Which keyword is used to create a subclass in Java?");
            q2.put("options", new JSONArray().put("super").put("extends").put("implements"));
            q2.put("answer", "extends");

            JSONObject q3 = new JSONObject();
            q3.put("question", "Which component is not part of Android UI?");
            q3.put("options", new JSONArray().put("TextView").put("RecyclerView").put("DataFrame"));
            q3.put("answer", "DataFrame");

            array.put(q1);
            array.put(q2);
            array.put(q3);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return array;
    }

    private void renderQuiz() {
        try {
            for (int i = 0; i < quizArray.length(); i++) {
                JSONObject qObj = quizArray.getJSONObject(i);
                String question = qObj.getString("question");
                JSONArray options = qObj.getJSONArray("options");

                TextView qText = new TextView(this);
                qText.setText((i + 1) + ". " + question);
                qText.setTextSize(18);
                qText.setPadding(0, 24, 0, 8);
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
            progressBar.setVisibility(View.GONE);
            quizContainer.setVisibility(View.VISIBLE);
            btnSubmit.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
