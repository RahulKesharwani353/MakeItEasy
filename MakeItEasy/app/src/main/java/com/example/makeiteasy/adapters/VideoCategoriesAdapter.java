package com.example.makeiteasy.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.makeiteasy.Keys;
import com.example.makeiteasy.R;
import com.example.makeiteasy.VideoSubCategory;
import com.example.makeiteasy.model.VidCatModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class VideoCategoriesAdapter extends BaseAdapter {


    private List<VidCatModel> vidCatList;
    private Context context;
    private ProgressDialog loadingBar;

    public VideoCategoriesAdapter(List<VidCatModel> vidCatList, Context context) {
        this.vidCatList = vidCatList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return vidCatList.size() ;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View view;
        if (convertView==null){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_1,parent,false);
        }
        else {
            view = convertView;
        }
        ((TextView) view.findViewById(R.id.cv1_text1)).setText(vidCatList.get(position).getVidCategory());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, VideoSubCategory.class);
                i.putExtra(Keys.vidCatKey,vidCatList.get(position).getVidCategory());
                v.getContext().startActivity(i);


            }
        });
        return view;
    }
}
