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
import com.example.makeiteasyadmins.adapters.VideoLectureAdapter;
import com.example.makeiteasyadmins.model.VidLecModel;
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

public class VideoLectures extends AppCompatActivity {

    private String subCat,cat;
    TextView t1;
    Animation bottomAnim;
    FloatingActionButton addCategoriesBtn;
    Button submit;
    RelativeLayout relativeLayout;
    RecyclerView recyclerView;
    int flag;
    FirebaseFirestore firebaseFirestore;
    VideoLectureAdapter adapter;
    TextInputEditText topic,duration,link;
    List<VidLecModel> vidLecList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos_lacture);

        subCat = getIntent().getStringExtra(Keys.vidSubCatKey);
        cat =  getIntent().getStringExtra(Keys.vidCatKey);
        t1 = findViewById(R.id.vid_lec_SubCat_title);


        t1.setText(subCat);


        topic = findViewById(R.id.add_vid_lec_topic);
        duration = findViewById(R.id.add_vid_lec_duration);
        link = findViewById(R.id.add_vid_lec_YTLink);
        recyclerView = findViewById(R.id.vid_lec_RecyclerView);
        submit = findViewById(R.id.add_Vid_lec_btn);
        relativeLayout = findViewById(R.id.addVideoLec_details);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        addCategoriesBtn = findViewById(R.id.add_New_VidLec);

        firebaseFirestore = FirebaseFirestore.getInstance();
        GridLayoutManager layoutManager = new GridLayoutManager(this,1);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new VideoLectureAdapter(vidLecList,this,cat,subCat);
        recyclerView.setAdapter(adapter);

        addData(adapter);

    }

    private void addData(final VideoLectureAdapter adapter) {
        firebaseFirestore.collection("VIDEOS").document(cat)
                .collection("VID_SUB_CAT").document(subCat)
                .collection("VID_LEC").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(VideoLectures.this, "data..", Toast.LENGTH_SHORT).show();
                            for (DocumentSnapshot documentSnapshot: Objects.requireNonNull(task.getResult())){
                                vidLecList.add(new VidLecModel(documentSnapshot.get("vidLecTopic").toString(),
                                        documentSnapshot.get("vidLecDuration").toString(),
                                        documentSnapshot.get("vidLecYTLInk").toString()));
                            }
                            adapter.notifyDataSetChanged();
                        }
                        else {
                            Toast.makeText(VideoLectures.this, "Error", Toast.LENGTH_SHORT).show();
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

                if (topic.getText().toString().equals("")||duration.getText().toString().equals("")||link.getText().toString().equals(""))
                {
                    Toast.makeText(VideoLectures.this, "Enter Detail First", Toast.LENGTH_SHORT).show();
                }
                else {
                    relativeLayout.setVisibility(View.GONE);
                    flag=0;
                    setData();
                }

            }
        });
    }

    private void setData() {


        Map<String, Object> user = new HashMap<>();
        user.put("vidLecTopic", topic.getText().toString());
        user.put("vidLecDuration", duration.getText().toString());
        user.put("vidLecYTLInk", link.getText().toString());

        firebaseFirestore.collection("VIDEOS").document(cat)
                .collection("VID_SUB_CAT").document(subCat)
                .collection("VID_LEC").document(topic.getText().toString())
                .set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                vidLecList.add(new VidLecModel(topic.getText().toString(), duration.getText().toString(),link.getText().toString()));
                adapter.notifyDataSetChanged();
                Toast.makeText(VideoLectures.this, "Successfully", Toast.LENGTH_SHORT).show();
                relativeLayout.setVisibility(View.GONE);
                flag=0;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(VideoLectures.this, "Failed", Toast.LENGTH_SHORT).show();

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