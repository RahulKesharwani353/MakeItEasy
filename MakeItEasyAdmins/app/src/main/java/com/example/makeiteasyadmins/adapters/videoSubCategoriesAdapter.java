package com.example.makeiteasyadmins.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makeiteasyadmins.AddVideoCategories;
import com.example.makeiteasyadmins.Keys;
import com.example.makeiteasyadmins.R;
import com.example.makeiteasyadmins.VideoLectures;
import com.example.makeiteasyadmins.VideoSubCategory;
import com.example.makeiteasyadmins.model.VidCatModel;
import com.example.makeiteasyadmins.model.VidSubCatModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class videoSubCategoriesAdapter extends RecyclerView.Adapter<videoSubCategoriesAdapter.myVH> {

    private List<VidSubCatModel> vidSubCatList;
    private  String selectedCat;
    private Context context;

    public videoSubCategoriesAdapter(List<VidSubCatModel> vidSubCatList, String selectedCat, Context context) {
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

       final videoSubCategoriesAdapter a =this;
        holder.subCat.setText(vidSubCatList.get(position).getVidSubCategory());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), VideoLectures.class);
                i.putExtra(Keys.vidSubCatKey,vidSubCatList.get(position).getVidSubCategory());
                i.putExtra(Keys.vidCatKey, selectedCat);
                v.getContext().startActivity(i);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData(vidSubCatList.get(position).getVidSubCategory(),a);
            }
        });

    }

    private void deleteData(final String subCat, final videoSubCategoriesAdapter adapter) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("VIDEOS").document(selectedCat)
                .collection("VID_SUB_CAT").document(subCat).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                            vidSubCatList.remove(new VidSubCatModel(subCat));
                            adapter.notifyDataSetChanged();
                            /*=========================================page refresh kiya hu============================*/

                            Intent i = new Intent(context, VideoSubCategory.class);
                            ((VideoSubCategory)context).finish();
                            i.putExtra(Keys.vidCatKey,selectedCat);
                            ((VideoSubCategory)context).overridePendingTransition(0, 0);
                            context.startActivity(i);
                            ((VideoSubCategory)context).overridePendingTransition(0, 0);
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
        return vidSubCatList.size();
    }

    public static class myVH extends RecyclerView.ViewHolder {
        TextView subCat;
        Button delete;
        public myVH(@NonNull View itemView) {
            super(itemView);
            subCat= itemView.findViewById(R.id.cv1_text1);
            delete = itemView.findViewById(R.id.cv1_delete_btn);
        }
    }
}
