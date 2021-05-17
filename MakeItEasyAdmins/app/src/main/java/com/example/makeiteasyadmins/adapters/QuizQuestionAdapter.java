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

import com.example.makeiteasyadmins.QuizCategory;
import com.example.makeiteasyadmins.QuizQuestion;
import com.example.makeiteasyadmins.R;
import com.example.makeiteasyadmins.model.QuizCategoryModel;
import com.example.makeiteasyadmins.model.QuizQuestionModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class QuizQuestionAdapter extends RecyclerView.Adapter<QuizQuestionAdapter.myVH> {

    private List<QuizQuestionModel> questionList;
    private Context context;
    private String subCat;

    public QuizQuestionAdapter(List<QuizQuestionModel> questionList, Context context, String subCat) {
        this.questionList = questionList;
        this.context = context;
        this.subCat = subCat;
    }

    @NonNull
    @Override
    public QuizQuestionAdapter.myVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_4,parent,false);
        return new myVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizQuestionAdapter.myVH holder, final int position) {
        holder.ques.setText(questionList.get(position).getQuestion());
        holder.op1.setText(questionList.get(position).getOption1());
        holder.op2.setText(questionList.get(position).getOption2());
        holder.op3.setText(questionList.get(position).getOption3());
        holder.op4.setText(questionList.get(position).getOption4());
        holder.correct.setText((String.valueOf(questionList.get(position).getCorrectOption())));
        holder.queNo.setText((String.valueOf(questionList.get(position).getQuestionNo())));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData(String.valueOf(questionList.get(position).getQuestionNo()));
            }
        });

    }

    private void deleteData(final String position) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("QUIZ").document(subCat)
                .collection("questions").document(position).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                            questionList.remove( new QuizQuestionModel(Integer.parseInt(position)));
                            /*=========================================page refresh kiya hu============================*/

                            Intent i = new Intent(context, QuizQuestion.class);
                            ((QuizQuestion)context).finish();
                            ((QuizQuestion)context).overridePendingTransition(0, 0);
                            context.startActivity(i);
                            ((QuizQuestion)context).overridePendingTransition(0, 0);
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
        return questionList.size();
    }

    public static class myVH extends RecyclerView.ViewHolder {
        TextView ques,op1,op2,op3,op4,correct,queNo;
        Button delete;

        public myVH(@NonNull View itemView) {
            super(itemView);
            ques = itemView.findViewById(R.id.display_question);
            op1 = itemView.findViewById(R.id.display_op1);
            op2 = itemView.findViewById(R.id.display_op2);
            op3 = itemView.findViewById(R.id.display_op3);
            op4 = itemView.findViewById(R.id.display_op4);
            queNo = itemView.findViewById(R.id.display_que_no);
            correct = itemView.findViewById(R.id.display_correctOp);
            delete = itemView.findViewById(R.id.que_delete);
        }
    }
}
