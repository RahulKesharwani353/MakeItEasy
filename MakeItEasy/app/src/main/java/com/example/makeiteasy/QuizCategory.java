package com.example.makeiteasy;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makeiteasy.adapters.QuizCategoriesAdapter;
import com.example.makeiteasy.model.QuizCategoryModel;
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

        recyclerView = findViewById(R.id.quiz_cat_recyclerVoew);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);


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

}