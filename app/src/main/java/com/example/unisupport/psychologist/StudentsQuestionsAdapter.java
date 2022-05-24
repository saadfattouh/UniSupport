package com.example.unisupport.psychologist;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unisupport.Constants;
import com.example.unisupport.R;
import com.example.unisupport.employee.AddReplyActivity;
import com.example.unisupport.employee.StudentsConsultationsAdapter;
import com.example.unisupport.model.StudentConsultation;
import com.example.unisupport.model.StudentQuestion;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentsQuestionsAdapter extends RecyclerView.Adapter<StudentsQuestionsAdapter.ViewHolder> {

    Context context;
    private List<StudentQuestion> items;
    private int status;

    // RecyclerView recyclerView;
    public StudentsQuestionsAdapter(Context context, ArrayList<StudentQuestion> items, int status) {
        this.context = context;
        this.items = items;
        this.status = status;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_student, parent, false);

        return new ViewHolder(listItem);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        StudentQuestion item = items.get(position);

        holder.name.setText(item.getS_name());

        holder.itemView.setOnClickListener(v -> {
            if(status == Constants.ANSWERED){
                LayoutInflater factory = LayoutInflater.from(context);
                final View view = factory.inflate(R.layout.question_details_dialog, null);
                final AlertDialog questionAnswerDialog = new AlertDialog.Builder(context).create();
                questionAnswerDialog.setView(view);

                //to look rounded
                questionAnswerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                TextView details = view.findViewById(R.id.question);
                details.setText(item.getQuestion());
                TextView answer = view.findViewById(R.id.answer);
                answer.setText(item.getAnswer());

                questionAnswerDialog.show();
            }else if(status == Constants.NOT_ANSWERED){
                Intent intent = new Intent(context, AddReplayPsychologistActivity.class);
                intent.putExtra("consultation_id", item.getId());
                intent.putExtra("question", item.getQuestion());
                intent.putExtra("student_id", item.getAsker_id());
                context.startActivity(intent);
            }

        });


    }



    @Override
    public int getItemCount() {
        return items.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public CircleImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.name);
            this.image  = itemView.findViewById(R.id.image);
        }

    }

}