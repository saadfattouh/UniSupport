package com.example.unisupport.student.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.unisupport.R;
import com.example.unisupport.model.Chat;
import com.example.unisupport.model.Tool;
import com.example.unisupport.student.ChatActivity;

import java.util.ArrayList;

public class ToolsAdapter extends RecyclerView.Adapter<ToolsAdapter.ViewHolder> {

    Context context;
    ArrayList<Tool> tools;

    public ToolsAdapter(Context context, ArrayList<Tool> tools) {
        this.context = context;
        this.tools = tools;
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

        Tool tool = tools.get(position);


        holder.link.setText(tool.getUrl());




    }

    @Override
    public int getItemCount() {
        return tools.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView link;

        public ViewHolder(View itemView) {
            super(itemView);
            this.link = itemView.findViewById(R.id.link);
        }
    }

}
