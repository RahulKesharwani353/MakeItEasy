package com.example.makeiteasy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.makeiteasy.model.QuizQuestionModel;
import com.example.makeiteasy.model.VidLecModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class QuizQuestions extends AppCompatActivity implements View.OnClickListener {

    private TextView quesCount, question, timer;
    private Button op1,op2,op3,op4,submit,next;
    int selectedOp =0;
    int index =0;
    int marks=0;
    private String selectCat;
    private FirebaseFirestore firebaseFirestore;
    private List<QuizQuestionModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_questions);

        selectCat = getIntent().getStringExtra(Keys.quizCategory);

        quesCount = findViewById(R.id.question_count);
        question  = findViewById(R.id.question_BOX);
        op1 = findViewById(R.id.o1);
        op2 = findViewById(R.id.o2);
        op3 = findViewById(R.id.o3);
        op4 = findViewById(R.id.o4);
        submit = findViewById(R.id.submit_quiz);
        next = findViewById(R.id.next_que);

        op1.setOnClickListener(this);
        op2.setOnClickListener(this);
        op3.setOnClickListener(this);
        op4.setOnClickListener(this);

        firebaseFirestore = FirebaseFirestore.getInstance();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    checkAns();
                    submit.setVisibility(View.GONE);
                    next.setVisibility(View.VISIBLE);

            }

        });

        getData();
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index<list.size()-1) {
                    next.setVisibility(View.GONE);
                    submit.setVisibility(View.VISIBLE);
                    nextQue();
                }
                else {
                    Intent i = new Intent(QuizQuestions.this,CompleteQuiz.class);
                    i.putExtra(Keys.totalQuizQue,String.valueOf(list.size()));
                    i.putExtra(Keys.totalQuizScore,String.valueOf(marks));
                    startActivity(i);
                    finish();
                }

            }
        });
    }

    private void checkAns() {

        switch (selectedOp){
            case 1:
                op1.setBackgroundResource(R.drawable.rounded_corner_bg_red);
                break;
            case 2: op2.setBackgroundResource(R.drawable.rounded_corner_bg_red);
                break;
            case 3: op3.setBackgroundResource(R.drawable.rounded_corner_bg_red);
                break;
            case 4: op4.setBackgroundResource(R.drawable.rounded_corner_bg_red);
                break;
            default:
        }


        switch (list.get(index).getCorrectOption()){
            case 1:
                op1.setBackgroundResource(R.drawable.rounded_corner_bg_green);
                break;
            case 2: op2.setBackgroundResource(R.drawable.rounded_corner_bg_green);
                break;
            case 3: op3.setBackgroundResource(R.drawable.rounded_corner_bg_green);
                break;
            case 4: op4.setBackgroundResource(R.drawable.rounded_corner_bg_green);
                break;
            default:
        }

        if (selectedOp == list.get(index).getCorrectOption()){
            marks++;
        }
    }

    private void nextQue() {
        if(index<list.size() ){
            index++;
            setData();
        }
        op1.setBackgroundResource(R.drawable.border_bg);
        op2.setBackgroundResource(R.drawable.border_bg);
        op3.setBackgroundResource(R.drawable.border_bg);
        op4.setBackgroundResource(R.drawable.border_bg);
    }

    private void setData() {
        quesCount.setText(String.valueOf(index+1));
        question.setText(list.get(index).getQuestion());
        op1.setText(list.get(index).getOption1());
        op2.setText(list.get(index).getOption2());
        op3.setText(list.get(index).getOption3());
        op4.setText(list.get(index).getOption4());
    }

    public void getData()
    {
        list = new ArrayList<QuizQuestionModel>();
            firebaseFirestore.collection("QUIZ").document(selectCat)
                .collection("questions").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (DocumentSnapshot documentSnapshot: Objects.requireNonNull(task.getResult())){
                                list.add(new QuizQuestionModel(documentSnapshot.get("question").toString(),
                                        documentSnapshot.get("option1").toString(),
                                        documentSnapshot.get("option2").toString(),
                                        documentSnapshot.get("option3").toString(),
                                        documentSnapshot.get("option4").toString(),
                                        Integer.parseInt(documentSnapshot.get("correctOption").toString())
                                        )
                                );

                            }
                        }
                        else {
                            Toast.makeText(QuizQuestions.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful())
                        setData();
                    else
                        Toast.makeText(QuizQuestions.this, "Data Error", Toast.LENGTH_SHORT).show();
                }
            });
    }


    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.o1:
               op1.setBackgroundResource(R.drawable.rounded_corner_bg_bright_blue);
               op2.setBackgroundResource(R.drawable.border_bg);
               op3.setBackgroundResource(R.drawable.border_bg);
               op4.setBackgroundResource(R.drawable.border_bg);
               selectedOp =1;
               break;
           case R.id.o2:
               selectedOp =2;
               op2.setBackgroundResource(R.drawable.rounded_corner_bg_bright_blue);
               op1.setBackgroundResource(R.drawable.border_bg);
               op3.setBackgroundResource(R.drawable.border_bg);
               op4.setBackgroundResource(R.drawable.border_bg);
               break;
           case R.id.o3:
               selectedOp =3;
               op3.setBackgroundResource(R.drawable.rounded_corner_bg_bright_blue);
               op2.setBackgroundResource(R.drawable.border_bg);
               op1.setBackgroundResource(R.drawable.border_bg);
               op4.setBackgroundResource(R.drawable.border_bg);
               break;
           case R.id.o4:
               op4.setBackgroundResource(R.drawable.rounded_corner_bg_bright_blue);
               op2.setBackgroundResource(R.drawable.border_bg);
               op3.setBackgroundResource(R.drawable.border_bg);
               op1.setBackgroundResource(R.drawable.border_bg);
               selectedOp =4;
               break;
           default:
       }
    }
}