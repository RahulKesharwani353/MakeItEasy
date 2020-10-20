package com.example.makeiteasy.adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makeiteasy.Keys;
import com.example.makeiteasy.R;
import com.example.makeiteasy.VideoLecture;
import com.example.makeiteasy.model.VidSubCatModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class VideoSubCategoriesAdapter extends RecyclerView.Adapter<VideoSubCategoriesAdapter.myVH> {

    private List<VidSubCatModel> vidSubCatList;
    private  String selectedCat;
    private Context context;

    public VideoSubCategoriesAdapter(List<VidSubCatModel> vidSubCatList, String selectedCat, Context context) {
        this.vidSubCatList = vidSubCatList;
        this.selectedCat = selectedCat;
        this.context = context;
    }

    @NonNull
    @Override
    public myVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_1,parent,false);
        return new myVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myVH holder, final int position) {

        final VideoSubCategoriesAdapter a =this;
        holder.subCat.setText(vidSubCatList.get(position).getVidSubCategory());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, VideoLecture.class);
                i.putExtra(Keys.vidSubCatKey,vidSubCatList.get(position).getVidSubCategory());
                i.putExtra(Keys.vidCatKey, selectedCat);
                context.startActivity(i);
            }
        });

    }


    @Override
    public int getItemCount() {
        return vidSubCatList.size();
    }

    public static class myVH extends RecyclerView.ViewHolder {
        TextView subCat;
        public myVH(@NonNull View itemView) {
            super(itemView);
            subCat= itemView.findViewById(R.id.cv1_text1);
        }
    }
}
