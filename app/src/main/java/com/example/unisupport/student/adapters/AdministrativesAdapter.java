package com.example.unisupport.student.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unisupport.R;
import com.example.unisupport.model.AdministrativeReply;
import com.google.android.gms.maps.model.Circle;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdministrativesAdapter extends RecyclerView.Adapter<AdministrativesAdapter.ViewHolder> {


    Context context;
    private List<AdministrativeReply> items;

    // RecyclerView recyclerView;
    public AdministrativesAdapter(Context context, ArrayList<AdministrativeReply> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_administrative, parent, false);

        return new ViewHolder(listItem);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        AdministrativeReply item = items.get(position);

        holder.name.setText(item.getA_name());

        holder.itemView.setOnClickListener(v -> {
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