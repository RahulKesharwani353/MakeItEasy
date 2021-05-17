package com.example.makeiteasy.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makeiteasy.Keys;
import com.example.makeiteasy.QuizCategory;
import com.example.makeiteasy.QuizQuestions;
import com.example.makeiteasy.R;
import com.example.makeiteasy.model.QuizCategoryModel;


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
                Intent i = new Intent(v.getContext(), QuizQuestions.class);
                i.putExtra(Keys.quizCategory, quizCatList.get(position).getQuizCategory());
                v.getContext().startActivity(i);
            }
        });

    }


    @Override
    public int getItemCount() {
        return quizCatList.size();
    }

    public static class myVH extends RecyclerView.ViewHolder {
        TextView subCat;
        ImageView img;
        public myVH(@NonNull View itemView) {
            super(itemView);
            subCat= itemView.findViewById(R.id.cv1_text1);
            img = itemView.findViewById(R.id.cv1_image1);
        }
    }
}
