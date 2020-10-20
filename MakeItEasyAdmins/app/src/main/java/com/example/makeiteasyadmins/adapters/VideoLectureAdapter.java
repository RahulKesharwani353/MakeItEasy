package com.example.makeiteasyadmins.adapters;

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

import com.example.makeiteasyadmins.Keys;
import com.example.makeiteasyadmins.R;
import com.example.makeiteasyadmins.VideoLectures;
import com.example.makeiteasyadmins.VideoSubCategory;
import com.example.makeiteasyadmins.model.VidLecModel;
import com.example.makeiteasyadmins.model.VidSubCatModel;
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

    public VideoLectureAdapter(List<VidLecModel> vidLecList) {
        this.vidLecList = vidLecList;
    }

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
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData(vidLecList.get(position).getVidLecTopic(),vidLecList.get(position)
                        .getVidLecDuration(),vidLecList.get(position).getVidLecYTLInk());
            }
        });


    }

    private void deleteData(final String topic,final String duration, final String link) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("VIDEOS").document(category)
                .collection("VID_SUB_CAT").document(subCategory)
                .collection("VID_LEC").document(topic).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                            vidLecList.remove(new VidLecModel(topic,duration,link));
                            /*=========================================page refresh kiya hu============================*/

                            Intent i = new Intent(context, VideoLectures.class);
                            ((VideoLectures)context).finish();
                            i.putExtra(Keys.vidCatKey,category);
                            i.putExtra(Keys.vidSubCatKey,subCategory);
                            ((VideoLectures)context).overridePendingTransition(0, 0);
                            context.startActivity(i);
                            ((VideoLectures)context).overridePendingTransition(0, 0);
                        }
                        else
                        {
                            Toast.makeText(context, "Failed, Try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    @Override
    public int getItemCount() {
        return vidLecList.size();
    }

    public class myVH extends RecyclerView.ViewHolder {
        TextView topic,duration;
        Button delete;
        public myVH(@NonNull View itemView) {
            super(itemView);
            topic = itemView.findViewById(R.id.cv3_text1);
            delete = itemView.findViewById(R.id.cv3_delete_btn);
            duration = itemView.findViewById(R.id.cv3_text2);
        }
    }
}
