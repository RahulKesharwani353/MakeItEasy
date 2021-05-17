package com.example.makeiteasy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class CompleteQuiz extends AppCompatActivity {

    private String marks,totalQue;
    TextView scoreDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_quiz);

        scoreDisplay = findViewById(R.id.quiz_score);
        marks = getIntent().getStringExtra(Keys.totalQuizScore);
        totalQue = getIntent().getStringExtra(Keys.totalQuizQue);

        scoreDisplay.setText(marks+"/"+totalQue);

    }
}