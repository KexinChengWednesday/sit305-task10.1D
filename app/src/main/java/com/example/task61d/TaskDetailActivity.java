package com.example.task61d;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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

        fetchQuizFromLLM();

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

    private void fetchQuizFromLLM() {
        String url = "http://10.0.2.2:5000/getQuiz?topic=android";

        OkHttpClient client
