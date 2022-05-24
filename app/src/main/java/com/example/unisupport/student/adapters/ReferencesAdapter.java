package com.example.unisupport.student.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unisupport.R;
import com.example.unisupport.model.Reference;
import com.example.unisupport.model.Tool;

import java.util.ArrayList;

public class ReferencesAdapter extends RecyclerView.Adapter<ReferencesAdapter.ViewHolder> {

    Context context;
    ArrayList<Reference> references;

    public ReferencesAdapter(Context context, ArrayList<Reference> references) {
        this.context = context;
        this.references = references;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.item_tool, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Reference reference = references.get(position);


        holder.link.setText(reference.getUrl());




    }

    @Override
    public int getItemCount() {
        return references.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView link;

        public ViewHolder(View itemView) {
            super(itemView);
            this.link = itemView.findViewById(R.id.link);
        }
    }

}
