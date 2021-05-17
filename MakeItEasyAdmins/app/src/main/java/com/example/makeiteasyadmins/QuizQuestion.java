package com.example.makeiteasyadmins;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.makeiteasyadmins.adapters.QuizCategoriesAdapter;
import com.example.makeiteasyadmins.adapters.QuizQuestionAdapter;
import com.example.makeiteasyadmins.model.QuizCategoryModel;
import com.example.makeiteasyadmins.model.QuizQuestionModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class QuizQuestion extends AppCompatActivity {

    Animation bottomAnim,bottomAnimOut;
    FloatingActionButton addNewQuizBtn;
    Button submit;
    String selectedCat;
    RelativeLayout relativeLayout;
    RecyclerView recyclerView;
    TextView toolbar;
    FirebaseFirestore firebaseFirestore;
    List<QuizQuestionModel> quizQueList;
    TextInputEditText quizQuestion,op1,op2,op3,op4,correctOp,queNo;
    QuizQuestionAdapter adapter;
    int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_question);

        selectedCat = getIntent().getStringExtra(Keys.quizCategory);

        quizQuestion = findViewById(R.id.add_quiz_que);
        recyclerView = findViewById(R.id.quiz_que_recyclerVoew);
        submit = findViewById(R.id.add_quiz_que_btn);
        relativeLayout = findViewById(R.id.addQuizQuestion_details);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        addNewQuizBtn = findViewById(R.id.add_ques_layout);


        op1 = findViewById(R.id.add_quiz_op1);
        op2 = findViewById(R.id.add_quiz_op2);
        op3 = findViewById(R.id.add_quiz_op3);
        op4 = findViewById(R.id.add_quiz_op4);
        correctOp = findViewById(R.id.add_quiz_que_correct);
        queNo = findViewById(R.id.add_quiz_que_no);


        quizQueList = new ArrayList<>();

        firebaseFirestore = FirebaseFirestore.getInstance();
        GridLayoutManager layoutManager = new GridLayoutManager(this,1);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new QuizQuestionAdapter(quizQueList,this,selectedCat);
        recyclerView.setAdapter(adapter);

        getData(adapter);

    }
    private void getData(final QuizQuestionAdapter adapter) {
        firebaseFirestore.collection("QUIZ").
                document(selectedCat).collection("questions").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (DocumentSnapshot documentSnapshot: Objects.requireNonNull(task.getResult())){
                                quizQueList.add(
                                        new QuizQuestionModel(documentSnapshot.get("question").toString(),
                                                documentSnapshot.get("option1").toString(),
                                                documentSnapshot.get("option2").toString(),
                                                documentSnapshot.get("option3").toString(),
                                                documentSnapshot.get("option4").toString(),
                                                Integer.parseInt(documentSnapshot.get("correctOption").toString()),
                                                Integer.parseInt(documentSnapshot.get("questionNo").toString())
                                        )
                                );
                            }
                            adapter.notifyDataSetChanged();
                        }
                        else {
                            Toast.makeText(QuizQuestion.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    @Override
    protected void onStart() {
        super.onStart();
        addNewQuizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayout.setVisibility(View.VISIBLE);
                relativeLayout.startAnimation(bottomAnim);
                flag=1;
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(QuizQuestion.this, "Sub-Category added...", Toast.LENGTH_SHORT).show();
                if (quizQuestion.getText().toString().equals("") || op1.getText().toString().equals("") ||
                        op2.getText().toString().equals("")|| op3.getText().toString().equals("")||
                        op4.getText().toString().equals("") || correctOp.getText().toString().equals("")
                        || queNo.getText().toString().equals("")
                ){
                    Toast.makeText(QuizQuestion.this, "enter detail", Toast.LENGTH_SHORT).show();
                    relativeLayout.setVisibility(View.GONE);
                    flag=0;
                }
                else{
                    setData();
                  }
            }
        });
    }

    private void setData() {

        Map<String, Object> user = new HashMap<>();
        user.put("question", quizQuestion.getText().toString());
        user.put("option1",op1.getText().toString());
        user.put("option2",op2.getText().toString());
        user.put("option3",op3.getText().toString());
        user.put("option4",op4.getText().toString());
        user.put("correctOption",correctOp.getText().toString());
        user.put("questionNo",queNo.getText().toString());

        firebaseFirestore.collection("QUIZ").document(selectedCat)
                .collection("questions").document(queNo.getText().toString())
                .set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                quizQueList.add(new QuizQuestionModel(quizQuestion.getText().toString(),
                        op1.getText().toString(),
                        op2.getText().toString(),
                        op3.getText().toString(),
                        op4.getText().toString(),
                        Integer.parseInt(correctOp.getText().toString()),
                        Integer.parseInt(queNo.getText().toString())
                        ));
                adapter.notifyDataSetChanged();
                Toast.makeText(QuizQuestion.this, "Successfully", Toast.LENGTH_SHORT).show();
                relativeLayout.setVisibility(View.GONE);
                flag=0;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(QuizQuestion.this, "Failed", Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public void onBackPressed() {
        if (flag==1){
            relativeLayout.setVisibility(View.GONE);
            flag=0;
        }
        else {
            super.onBackPressed();
            return;
        }
    }

}