package com.example.unisupport.employee;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.unisupport.R;
import com.example.unisupport.api.Urls;
import com.example.unisupport.student.ChatActivity;
import com.example.unisupport.utils.SharedPrefManager;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class AddReplyActivity extends AppCompatActivity {

    Button mSaveBtn;
    TextInputEditText mContent;

    int consultation_id;
    String question;
    int myId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reply);

        Intent sender = getIntent();
        if(sender != null){
            consultation_id = sender.getIntExtra("consultation_id", -1);
            question = sender.getStringExtra("consultation");
        }
        myId = SharedPrefManager.getInstance(this).getUserId();

        TextView consultation = findViewById(R.id.consultation);
        consultation.setText(question);

        mContent = findViewById(R.id.reply);

        mSaveBtn = findViewById(R.id.save);

        mSaveBtn.setOnClickListener(v -> {
            if(mContent.getText().toString().isEmpty()){
                Toast.makeText(this, "you can't send an empty reply!", Toast.LENGTH_SHORT).show();
            }else {
                replyToQuestion(mContent.getText().toString().trim());
            }
        });


    }

    private void replyToQuestion(String reply) {

        ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        String url = Urls.BASE_URL + Urls.EMPLOYEE_REPLY_TO_STUDENTS_CONSULTATIONS;

        AndroidNetworking.post(url)
                .addBodyParameter("consultation_id", String.valueOf(consultation_id))
                .addBodyParameter("employee_id", String.valueOf(myId))
                .addBodyParameter("replay", reply)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();

                        try {
                            String message = response.getString("message");
                            Toast.makeText(AddReplyActivity.this, message, Toast.LENGTH_SHORT).show();
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        pDialog.dismiss();
                        Toast.makeText(AddReplyActivity.this, error.getErrorBody(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}