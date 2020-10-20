package com.example.makeiteasyadmins.adapters;

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

import com.example.makeiteasyadmins.AddVideoCategories;
import com.example.makeiteasyadmins.AdminDashboard;
import com.example.makeiteasyadmins.AdminLogin;
import com.example.makeiteasyadmins.Keys;
import com.example.makeiteasyadmins.R;
import com.example.makeiteasyadmins.VideoSubCategory;
import com.example.makeiteasyadmins.model.VidCatModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class videoCategoriesAdapter extends BaseAdapter {

    private List<VidCatModel> vidCatList;
    private Context context;
    private ProgressDialog loadingBar;

    public videoCategoriesAdapter(List<VidCatModel> vidCatList, Context context) {
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
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_2,parent,false);
        }
        else {
            view = convertView;
        }
        ((TextView) view.findViewById(R.id.cv2_text1)).setText(vidCatList.get(position).getVidCategory());
        loadingBar= new ProgressDialog(context);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(parent.getContext(), VideoSubCategory.class);
                i.putExtra(Keys.vidCatKey,vidCatList.get(position).getVidCategory());
                parent.getContext().startActivity(i);
            }
        });
        final Button deleteCat = view.findViewById(R.id.cv2_delete_btn);
        deleteCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setMessage("All the content of this playlist will be delete permanently and cannot be restore.\n\nAre you sure to Delete")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteData(vidCatList.get(position).getVidCategory(),parent);
                    }
                })
                        .setNegativeButton("Cancel",null);

                AlertDialog alert = builder.create();
                alert.show();

            }
        });


        return view;

    }

   void deleteData(final String rmCat, final ViewGroup vcon){


       loadingBar.setTitle("Deleting");
       loadingBar.setMessage("please Wait");
       loadingBar.setCanceledOnTouchOutside(false);
       loadingBar.show();

       FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
       firebaseFirestore.collection("VIDEOS").document(rmCat).delete()
               .addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       if (task.isSuccessful()){
                           Toast.makeText(vcon.getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                           vidCatList.remove(new VidCatModel(rmCat));

/*=========================================page refresh kiya hu============================*/
                           Intent i = new Intent(context,AddVideoCategories.class);
                           loadingBar.dismiss();
                           ((AddVideoCategories)context).finish();
                           ((AddVideoCategories)context).overridePendingTransition(0, 0);
                           context.startActivity(i);
                           ((AddVideoCategories)context).overridePendingTransition(0, 0);
                       }
                       else
                       {
                           Toast.makeText(context, "Failed, Try again", Toast.LENGTH_SHORT).show();
                           loadingBar.dismiss();
                       }
                   }
               });
    }



}
