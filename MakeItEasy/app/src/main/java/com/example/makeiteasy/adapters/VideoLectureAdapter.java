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
import com.example.makeiteasy.PlayerActivity;
import com.example.makeiteasy.R;
import com.example.makeiteasy.model.VidLecModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class VideoLectureAdapter extends RecyclerView.Adapter<VideoLectureAdapter.myVH>{
    private List<VidLecModel> vidLecList;
    private Context context;
   private String category;

    public VideoLectureAdapter(List<VidLecModel> vidLecList, Context context, String category, String subCategory) {
        this.vidLecList = vidLecList;
        this.context = context;
        this.category = category;
        this.subCategory = subCategory;
    }

    private String subCategory;


    @NonNull
    @Override
    public myVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_3,parent,false);
        return new myVH(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final myVH holder, final int position) {

        String vidLecTopic = vidLecList.get(position).getVidLecTopic();
        final String vidLecDuration = vidLecList.get(position).getVidLecDuration();
        String vidYTLink = vidLecList.get(position).getVidLecYTLInk();

        holder.topic.setText(vidLecTopic);
        holder.duration.setText(vidLecDuration);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent yt = new Intent(context, PlayerActivity.class);
                yt.putExtra(Keys.ytLink,vidLecList.get(position).getVidLecYTLInk());
                context.startActivity(yt);
            }
        });


    }

    @Override
    public int getItemCount() {
        return vidLecList.size();
    }

    public static class myVH extends RecyclerView.ViewHolder {
        TextView topic,duration;
        public myVH(@NonNull View itemView) {
            super(itemView);
            topic = itemView.findViewById(R.id.cv3_text1);
            duration = itemView.findViewById(R.id.cv3_text2);
        }
    }
}
