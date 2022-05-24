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
import com.example.unisupport.student.ChatActivity;

import java.util.ArrayList;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ViewHolder> {

    Context context;
    ArrayList<Chat> chats;

    public ChatsAdapter(Context context, ArrayList<Chat> chats) {
        this.context = context;
        this.chats = chats;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.item_chat, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Chat chat = chats.get(position);

        if(chat.getUserImageUrl() == null){
            Glide.with(context)
                    .load(context.getResources().getDrawable(R.drawable.ic_person))
                    .into(holder.image);
        }else{
            Glide.with(context)
                    .load(chat.getUserImageUrl())
                    .into(holder.image);
        }

        holder.name.setText(chat.getUserName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("his_id", chat.getHisId());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;
        public TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            this.image = itemView.findViewById(R.id.person_profile_image);
            this.name = itemView.findViewById(R.id.person_name);
        }
    }

}
