package com.example.makeiteasy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.makeiteasy.adapters.VideoSubCategoriesAdapter;
import com.example.makeiteasy.model.VidSubCatModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VideoSubCategory extends AppCompatActivity {

    String selectedCat;
    RecyclerView recyclerView;
    TextView toolbar;
    FirebaseFirestore firebaseFirestore;
    List<VidSubCatModel> vidSubCatList ;
    VideoSubCategoriesAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_sub_category);

        
        selectedCat = getIntent().getStringExtra(Keys.vidCatKey);
        toolbar =findViewById(R.id.vid_sub_cat_title);
        toolbar.setText(selectedCat);

        recyclerView = findViewById(R.id.vid_Sub_cat_RecyclerView);


        vidSubCatList = new ArrayList<>();

        firebaseFirestore = FirebaseFirestore.getInstance();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new VideoSubCategoriesAdapter(vidSubCatList,selectedCat,VideoSubCategory.this);
        recyclerView.setAdapter(adapter);

        getData(adapter);

    }

    private void getData(final VideoSubCategoriesAdapter adapter) {
        firebaseFirestore.collection("VIDEOS").document(selectedCat)
                .collection("VID_SUB_CAT").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
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

}