package com.example.unisupport.student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.unisupport.Constants;
import com.example.unisupport.R;
import com.example.unisupport.api.Urls;
import com.example.unisupport.model.BaseMessage;
import com.example.unisupport.model.PsychologistReply;
import com.example.unisupport.psychologist.MainActivityPsychologist;
import com.example.unisupport.student.adapters.ChatMessagesAdapter;
import com.example.unisupport.student.adapters.PsychologistsRepliesAdapter;
import com.example.unisupport.utils.SharedPrefManager;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    int hisId;
    int myId;

    RecyclerView mMessagesList;
    ArrayList<BaseMessage> messages;
    ChatMessagesAdapter messagesAdapter;

    TextInputEditText messageET;
    ImageView sendBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        hisId = getIntent().getIntExtra("his_id", -1);
        myId = SharedPrefManager.getInstance(this).getUserId();

        mMessagesList = findViewById(R.id.messages_list);
        messageET = findViewById(R.id.message_edit_text);
        sendBtn = findViewById(R.id.send_btn);

        sendBtn.setOnClickListener(v -> {

            String message = messageET.getText().toString();
            messageET.setText("");
            if(TextUtils.isEmpty(message)){

            }else{
                sendMessage(message);
            }

        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllMessages();
    }

    private void sendMessage(String message) {

        String url = Urls.BASE_URL + Urls.SEND_CHAT_MESSAGE;

        AndroidNetworking.post(url)
                .setPriority(Priority.MEDIUM)
                .addBodyParameter("from_user", String.valueOf(myId))
                .addBodyParameter("to_user", String.valueOf(hisId))
                .addBodyParameter("is_private", String.valueOf(Constants.PRIVATE_MESSAGE))
                .addBodyParameter("content", message)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        JSONObject obj = response;
                        try {

                            Toast.makeText(ChatActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            messages.add(new BaseMessage(1, true, message));
                            messagesAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.e("Error", error.getErrorBody());

                    }
                });

    }

    private void getAllMessages() {

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Processing Please wait...");
        pDialog.show();

        String url = Urls.BASE_URL + Urls.GET_CHAT_MESSAGES;

        messages = new ArrayList<>();

        AndroidNetworking.get(url)
                .setPriority(Priority.MEDIUM)
                .addQueryParameter("from_user", String.valueOf(myId))
                .addQueryParameter("to_user", String.valueOf(hisId))
                .addQueryParameter("is_private", String.valueOf(Constants.PRIVATE_MESSAGE))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        pDialog.dismiss();

                        try {
                            //converting response to json object
                            JSONObject obj = response;
                            String message = response.getString("message");

                            //if no error in response
                            Toast.makeText(ChatActivity.this, message, Toast.LENGTH_SHORT).show();
                            //getting the user from the response
                            JSONArray array = obj.getJSONArray("data");
                            BaseMessage msg;
                            for (int i = 0; i < array.length(); i++){
                                JSONObject item = array.getJSONObject(i);
                                msg = new BaseMessage();
                                msg.setId(item.getInt("id"));
                                msg.setFromMe(item.getInt("from_user") == myId);
                                msg.setMessage(item.getString("content"));
                                msg.setDate(item.getString("date"));

                                messages.add(msg);
                            }
                            messagesAdapter = new ChatMessagesAdapter(messages, ChatActivity.this);
                            mMessagesList.setAdapter(messagesAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        pDialog.dismiss();
                        Toast.makeText(ChatActivity.this, error.getErrorBody(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}