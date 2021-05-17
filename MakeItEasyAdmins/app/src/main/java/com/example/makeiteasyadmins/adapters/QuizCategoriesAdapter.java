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
import com.example.makeiteasyadmins.QuizCategory;
import com.example.makeiteasyadmins.QuizQuestion;
import com.example.makeiteasyadmins.R;
import com.example.makeiteasyadmins.VideoLectures;
import com.example.makeiteasyadmins.VideoSubCategory;
import com.example.makeiteasyadmins.model.QuizCategoryModel;
import com.example.makeiteasyadmins.model.VidSubCatModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class QuizCategoriesAdapter extends RecyclerView.Adapter<QuizCategoriesAdapter.myVH> {

    private List<QuizCategoryModel> quizCatList;
    private Context context;

    public QuizCategoriesAdapter(List<QuizCategoryModel> quizCatList, Context context) {
        this.quizCatList = quizCatList;
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

       final QuizCategoriesAdapter a =this;
        holder.subCat.setText(quizCatList.get(position).getQuizCategory());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), QuizQuestion.class);
                i.putExtra(Keys.quizCategory, quizCatList.get(position).getQuizCategory());
                v.getContext().startActivity(i);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData(quizCatList.get(position).getQuizCategory(),a);
            }
        });

    }

    private void deleteData(final String subCat, final QuizCategoriesAdapter adapter) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("QUIZ").document(subCat).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                            quizCatList.remove(new QuizCategoryModel(subCat));
                            adapter.notifyDataSetChanged();
                            /*=========================================page refresh kiya hu============================*/

                            Intent i = new Intent(context, QuizCategory.class);
                            ((QuizCategory)context).finish();
                            ((QuizCategory)context).overridePendingTransition(0, 0);
                            context.startActivity(i);
                            ((QuizCategory)context).overridePendingTransition(0, 0);
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
        return quizCatList.size();
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
