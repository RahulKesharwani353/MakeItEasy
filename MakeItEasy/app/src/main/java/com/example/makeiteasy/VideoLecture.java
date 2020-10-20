package com.example.makeiteasy;

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

import com.example.makeiteasy.adapters.VideoLectureAdapter;
import com.example.makeiteasy.model.VidLecModel;
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

public class VideoLecture extends AppCompatActivity {

    private String subCat,cat;
    TextView t1,t2;
    RecyclerView recyclerView;
    FirebaseFirestore firebaseFirestore;
    VideoLectureAdapter adapter;
    List<VidLecModel> vidLecList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_lecture);
        subCat = getIntent().getStringExtra(Keys.vidSubCatKey);
        cat =  getIntent().getStringExtra(Keys.vidCatKey);
        t1 = findViewById(R.id.vid_lec_SubCat_title);
        t1.setText(subCat);


        recyclerView = findViewById(R.id.vid_lec_RecyclerView);
        firebaseFirestore = FirebaseFirestore.getInstance();
        GridLayoutManager layoutManager = new GridLayoutManager(VideoLecture.this,1);
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
                            for (DocumentSnapshot documentSnapshot: Objects.requireNonNull(task.getResult())){
                                vidLecList.add(new VidLecModel(documentSnapshot.get("vidLecTopic").toString(),
                                        documentSnapshot.get("vidLecDuration").toString(),
                                        documentSnapshot.get("vidLecYTLInk").toString()
                                        ));

                            }
                            adapter.notifyDataSetChanged();
                        }
                        else {
                            Toast.makeText(VideoLecture.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}