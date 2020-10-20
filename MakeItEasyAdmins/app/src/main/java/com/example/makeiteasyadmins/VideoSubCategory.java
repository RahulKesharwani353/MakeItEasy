package com.example.makeiteasyadmins;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.makeiteasyadmins.adapters.videoSubCategoriesAdapter;
import com.example.makeiteasyadmins.model.VidCatModel;
import com.example.makeiteasyadmins.model.VidSubCatModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class VideoSubCategory extends AppCompatActivity {

    Animation bottomAnim,bottomAnimOut;
    FloatingActionButton addCategoriesBtn;
    Button submit;
    String selectedCat;
    RelativeLayout relativeLayout;
    RecyclerView recyclerView;
    TextView toolbar;
    FirebaseFirestore firebaseFirestore;
    List<VidSubCatModel> vidSubCatList ;
    TextInputEditText subCatName;
    videoSubCategoriesAdapter adapter;
    int flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_sub_category);
        selectedCat = getIntent().getStringExtra(Keys.vidCatKey);
        toolbar =findViewById(R.id.vid_sub_cat_title);
        toolbar.setText(selectedCat);


        subCatName = findViewById(R.id.vid_add_Sub_cat_name);
        recyclerView = findViewById(R.id.vid_Sub_cat_RecyclerView);
        submit = findViewById(R.id.add_Vid_Sub_cat_btn);
        relativeLayout = findViewById(R.id.addVideoSubCategories_details);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        addCategoriesBtn = findViewById(R.id.add_New_VidSubCategories);


        vidSubCatList = new ArrayList<>();

        firebaseFirestore = FirebaseFirestore.getInstance();
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new videoSubCategoriesAdapter(vidSubCatList,selectedCat,VideoSubCategory.this);
        recyclerView.setAdapter(adapter);

        getData(adapter);

    }

    private void getData(final videoSubCategoriesAdapter adapter) {
        firebaseFirestore.collection("VIDEOS").document(selectedCat)
                .collection("VID_SUB_CAT").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(VideoSubCategory.this, "data aa gaya", Toast.LENGTH_SHORT).show();
                            for (DocumentSnapshot documentSnapshot: Objects.requireNonNull(task.getResult())){
                                vidSubCatList.add(new VidSubCatModel(documentSnapshot.get("vidSubCategory").toString()));
                            }
                            adapter.notifyDataSetChanged();
                        }
                        else {
                            Toast.makeText(VideoSubCategory.this, "Error", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(VideoSubCategory.this, "Sub-Category added...", Toast.LENGTH_SHORT).show();
                if (!subCatName.getText().toString().equals(""))
                    setData();
                else{
                    Toast.makeText(VideoSubCategory.this, "enter detail", Toast.LENGTH_SHORT).show();
                relativeLayout.setVisibility(View.GONE);
                flag=0;}
            }
        });
    }

    private void setData() {

        Map<String, Object> user = new HashMap<>();
        user.put("vidSubCategory", subCatName.getText().toString());

        firebaseFirestore.collection("VIDEOS").document(selectedCat).collection("VID_SUB_CAT").document(subCatName.getText().toString())
                .set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                vidSubCatList.add(new VidSubCatModel(subCatName.getText().toString()));
                adapter.notifyDataSetChanged();
                Toast.makeText(VideoSubCategory.this, "Successfully", Toast.LENGTH_SHORT).show();
                relativeLayout.setVisibility(View.GONE);
                flag=0;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(VideoSubCategory.this, "Failed", Toast.LENGTH_SHORT).show();

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