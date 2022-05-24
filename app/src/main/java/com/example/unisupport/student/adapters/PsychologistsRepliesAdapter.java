package com.example.unisupport.student.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unisupport.R;
import com.example.unisupport.model.PsychologistReply;
import com.example.unisupport.student.PsychologistReplyViewActivity;

import java.util.ArrayList;
import java.util.List;

public class PsychologistsRepliesAdapter extends RecyclerView.Adapter<PsychologistsRepliesAdapter.ViewHolder> {


    Context context;
    private List<PsychologistReply> items;

    // RecyclerView recyclerView;
    public PsychologistsRepliesAdapter(Context context, ArrayList<PsychologistReply> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_psychologist, parent, false);

        return new ViewHolder(listItem);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        PsychologistReply item = items.get(position);

        holder.name.setText(item.getP_name());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PsychologistReplyViewActivity.class);
            intent.putExtra("content", item.getAnswer());
            intent.putExtra("his_id", item.getPsychologist_id());
            intent.putExtra("question_id", item.getId());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.name);
        }

    }


}