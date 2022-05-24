package com.example.unisupport.student.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.unisupport.Constants;
import com.example.unisupport.R;
import com.example.unisupport.api.Urls;
import com.example.unisupport.model.Chat;
import com.example.unisupport.student.PublicChatActivity;
import com.example.unisupport.student.StudentsActivity;
import com.example.unisupport.student.adapters.ChatsAdapter;
import com.example.unisupport.utils.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChatsFragment extends Fragment {

    ChatsAdapter mAdapter;
    ArrayList<Chat> chats;
    RecyclerView mList;

    int myId;
    int userType;

    Context context;

    public ChatsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context  = context;
        myId = SharedPrefManager.getInstance(context).getUserId();
        userType = SharedPrefManager.getInstance(context).getUserType();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mList = view.findViewById(R.id.chats_list);

        if(userType == Constants.TYPE_PSYCHOLOGIST){
            view.findViewById(R.id.public_chat).setVisibility(View.GONE);
        }else {
            view.findViewById(R.id.public_chat).setOnClickListener(v -> {
                Intent intent = new Intent(context, PublicChatActivity.class);
                startActivity(intent);
            });
        }



        view.findViewById(R.id.start_chat_btn).setOnClickListener(v -> {
            Intent intent = new Intent(context, StudentsActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getChatsByUser();
    }

    void getChatsByUser(){

        String url = Urls.BASE_URL + Urls.GET_USER_CHATS;

        chats = new ArrayList<>();

        AndroidNetworking.get(url)
                .setPriority(Priority.MEDIUM)
                .addQueryParameter("user_id", String.valueOf(SharedPrefManager.getInstance(context).getUserId()))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                                     @Override
                                     public void onResponse(JSONObject response) {
                                         // do anything with response
                                         try {
                                             //converting response to json object
                                             JSONObject obj = response;
                                             String message = response.getString("message");

                                             //if no error in response
                                             Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                             //getting the user from the response
                                             JSONArray array = obj.getJSONArray("data");
                                             Chat chat;
                                             for (int i = 0; i < array.length(); i++){
                                                 JSONObject item = array.getJSONObject(i);
                                                 chat = new Chat();
                                                 if(item.isNull("from_user") || item.isNull("to_user")){
                                                     continue;
                                                 }
                                                 if(item.getInt("from_user") == myId){
                                                     chat.setHisId(item.getInt("to_user"));
                                                     JSONObject u = item.getJSONObject("to");
                                                     chat.setUserImageUrl(Urls.PUBLIC_FOLDER + u.getString("profile_photo_url"));
                                                     chat.setUserName(u.getString("first_name") + u.getString("last_name"));
                                                 }else if(item.getInt("to_user") == myId){
                                                     chat.setHisId(item.getInt("from_user"));
                                                     JSONObject u = item.getJSONObject("from");
                                                     chat.setUserImageUrl(Urls.PUBLIC_FOLDER + u.getString("profile_photo_url"));
                                                     chat.setUserName(u.getString("first_name") + u.getString("last_name"));
                                                 }

                                                 if (!chats.contains(chat))
                                                     chats.add(chat);
                                             }
                                             mAdapter = new ChatsAdapter(context, chats);
                                             mList.setAdapter(mAdapter);
                                         } catch (JSONException e) {
                                             e.printStackTrace();
                                         }
                                     }
                                     @Override
                                     public void onError(ANError error) {
                                         // handle error
                                         Toast.makeText(context, error.getErrorBody(), Toast.LENGTH_SHORT).show();
                                     }
                                 }
                );

    }

}