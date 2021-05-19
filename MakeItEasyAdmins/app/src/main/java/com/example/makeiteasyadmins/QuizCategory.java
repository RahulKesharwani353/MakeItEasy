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
import com.example.makeiteasyadmins.adapters.videoSubCategoriesAdapter;
import com.example.makeiteasyadmins.model.QuizCategoryModel;
import com.example.makeiteasyadmins.model.VidSubCatModel;
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

public class QuizCategory extends AppCompatActivity {

    Animation bottomAnim,bottomAnimOut;
    FloatingActionButton addCategoriesBtn;
    Button submit;
    String selectedCat;
    RelativeLayout relativeLayout;
    RecyclerView recyclerView;
    TextView toolbar;
    FirebaseFirestore firebaseFirestore;
    List<QuizCategoryModel> quizCatList;
    TextInputEditText quizCatName;
    QuizCategoriesAdapter adapter;
    int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_category);


        quizCatName = findViewById(R.id.add_quiz_cat_name);
        recyclerView = findViewById(R.id.quiz_cat_recyclerVoew);
        submit = findViewById(R.id.add_quiz_category_btn);
        relativeLayout = findViewById(R.id.addQuizCategories_details);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        addCategoriesBtn = findViewById(R.id.add_New_QuizCategories);


        quizCatList = new ArrayList<>();

        firebaseFirestore = FirebaseFirestore.getInstance();
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new QuizCategoriesAdapter(quizCatList,this);
        recyclerView.setAdapter(adapter);

        getData(adapter);

    }

    private void getData(final QuizCategoriesAdapter adapter) {
        firebaseFirestore.collection("QUIZ").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (DocumentSnapshot documentSnapshot: Objects.requireNonNull(task.getResult())){
                                quizCatList.add(new QuizCategoryModel(documentSnapshot.get("quizCategory").toString()));
                            }
                            adapter.notifyDataSetChanged();
                        }
                        else {
                            Toast.makeText(QuizCategory.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



    @Override
    protected void onStart() {
        super.onStart();
        addCategoriesBtn.setOnClickListener(new View.OnClickListener() {
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
                if (!quizCatName.getText().toString().equals(""))
                    setData();
                else{
                    Toast.makeText(QuizCategory.this, "enter detail", Toast.LENGTH_SHORT).show();
                    relativeLayout.setVisibility(View.GONE);
                    flag=0;}
            }
        });
    }

    private void setData() {

        Map<String, Object> user = new HashMap<>();
        user.put("quizCategory", quizCatName.getText().toString());

        firebaseFirestore.collection("QUIZ").document(quizCatName.getText().toString())
                .set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                quizCatList.add(new QuizCategoryModel(quizCatName.getText().toString()));
                adapter.notifyDataSetChanged();
                Toast.makeText(QuizCategory.this, "Successfully ADDED", Toast.LENGTH_SHORT).show();
                relativeLayout.setVisibility(View.GONE);
                flag=0;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(QuizCategory.this, "Failed", Toast.LENGTH_SHORT).show();

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