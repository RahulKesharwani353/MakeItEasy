package com.example.makeiteasyadmins;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.makeiteasyadmins.adapters.videoCategoriesAdapter;
import com.example.makeiteasyadmins.model.VidCatModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddVideoCategories extends AppCompatActivity {

    Animation bottomAnim,bottomAnimOut;
    FloatingActionButton addCategoriesBtn;
    Button submit;
    TextInputEditText catName;
    RelativeLayout relativeLayout;
    GridView gridView;
    FirebaseFirestore firebaseFirestore;
    videoCategoriesAdapter adapter;
    private List<VidCatModel> vidCatList;
    ProgressDialog loadingBar;
    int flag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video_categories);

        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        bottomAnimOut = AnimationUtils.loadAnimation(this, R.anim.bottom_animation_out);

        loadingBar = new ProgressDialog(this);
        catName = findViewById(R.id.add_cat_name);
        submit = findViewById(R.id.add_Video_category_btn);
        gridView = findViewById(R.id.vid_cat_gridView);
        relativeLayout = findViewById(R.id.addVideoCategories_details);
        addCategoriesBtn = findViewById(R.id.add_Nee_VideoCategories);



        vidCatList = new ArrayList<>();

        firebaseFirestore = FirebaseFirestore.getInstance();
        adapter = new videoCategoriesAdapter(vidCatList, AddVideoCategories.this);
        gridView.setAdapter(adapter);
        getData(adapter);

    }

    private void getData(final videoCategoriesAdapter adapter) {
        firebaseFirestore.collection("VIDEOS").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){

                            for (QueryDocumentSnapshot documentSnapshot: task.getResult()){
                                vidCatList.add(new VidCatModel(documentSnapshot.get("vidCategory").toString()));
                            }
                            adapter.notifyDataSetChanged();
                        }
                        else {
                            Toast.makeText(AddVideoCategories.this, "Error", Toast.LENGTH_SHORT).show();
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

                if (!catName.getText().toString().equals("")) {

                    loadingBar.setTitle("Adding");
                    loadingBar.setMessage("please Wait");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();
                    addData();

                }
                else
                    Toast.makeText(AddVideoCategories.this, "Enter The Category First", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void addData() {

        Map<String, Object> user = new HashMap<>();
        user.put("vidCategory", catName.getText().toString());

        firebaseFirestore.collection("VIDEOS").document(catName.getText().toString())
                .set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                vidCatList.add(new VidCatModel(catName.getText().toString()));
                adapter.notifyDataSetChanged();
                Toast.makeText(AddVideoCategories.this, "Successfully", Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
                relativeLayout.setVisibility(View.GONE);
                flag=0;

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddVideoCategories.this, "Failed", Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
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