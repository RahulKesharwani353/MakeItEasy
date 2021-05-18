package com.example.makeiteasy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.makeiteasy.adapters.VideoCategoriesAdapter;
import com.example.makeiteasy.model.VidCatModel;
import com.google.android.gms.tasks.OnCompleteListener;
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

public class VideoCategory extends AppCompatActivity {

    GridView gridView;
    FirebaseFirestore firebaseFirestore;
    VideoCategoriesAdapter adapter;
    private List<VidCatModel> vidCatList;
    ProgressBar loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_category);

        loadingBar = findViewById(R.id.vc_progressBar);
        gridView = findViewById(R.id.vid_cat_gridView);



        vidCatList = new ArrayList<>();

        firebaseFirestore = FirebaseFirestore.getInstance();
        adapter = new VideoCategoriesAdapter(vidCatList, VideoCategory.this);
        gridView.setAdapter(adapter);
        getData(adapter);

    }

    private void getData(final VideoCategoriesAdapter adapter) {
        firebaseFirestore.collection("VIDEOS").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot: task.getResult()){
                                vidCatList.add(new VidCatModel(documentSnapshot.get("vidCategory").toString()));
                            }
                            loadingBar.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();

                        }
                        else {
                            Toast.makeText(VideoCategory.this, "Server Error", Toast.LENGTH_SHORT).show();
                            loadingBar.setVisibility(View.GONE);
                        }
                    }

                });
    }



}