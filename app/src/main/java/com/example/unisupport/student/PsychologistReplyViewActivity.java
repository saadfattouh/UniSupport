package com.example.unisupport.student;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
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
import com.example.unisupport.Constants;
import com.example.unisupport.R;
import com.example.unisupport.api.Urls;
import com.example.unisupport.model.BaseMessage;
import com.example.unisupport.utils.SharedPrefManager;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class PsychologistReplyViewActivity extends AppCompatActivity {

    Button mEvalBtn, mMessageBtn;
    TextInputEditText mContent;


    String content;
    int questionId;
    int psychologist_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_psychologist_reply_view);

        Intent sender = getIntent();
        if(sender != null){
            content = sender.getStringExtra("content");
            questionId = sender.getIntExtra("question_id", -1);
            psychologist_id = sender.getIntExtra("his_id", -1);
        }

        mContent = findViewById(R.id.description);

        mEvalBtn = findViewById(R.id.evaluate);
        mMessageBtn = findViewById(R.id.message);

        mContent.setEnabled(false);

        mContent.setText(content);

        mEvalBtn.setOnClickListener(v -> {
            LayoutInflater factory = LayoutInflater.from(this);
            final View view = factory.inflate(R.layout.dialog_evaluation, null);
            final AlertDialog eualuateDialog = new AlertDialog.Builder(this).create();
            eualuateDialog.setView(view);
            eualuateDialog.setCancelable(false);

            //to look rounded
            eualuateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            RatingBar ratingBar = view.findViewById(R.id.rating);
            TextView yes = view.findViewById(R.id.yes_btn);
            TextView no = view.findViewById(R.id.no_btn);

            yes.setOnClickListener(v1 -> {
                float userRating = ratingBar.getRating();
                updateQuestion(questionId, userRating);
                eualuateDialog.dismiss();
            });

            no.setOnClickListener(v1 -> {
                eualuateDialog.dismiss();
            });

            eualuateDialog.show();
        });

        mMessageBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra("his_id", psychologist_id);
            startActivity(intent);
            finish();
        });


    }

    private void updateQuestion(int questionId, float userRating) {
        String url = Urls.BASE_URL + Urls.EVALUATE_PSYCHOLOGIST;

        AndroidNetworking.post(url)
                .setPriority(Priority.MEDIUM)
                .addBodyParameter("degree", String.valueOf(userRating))
                .addBodyParameter("consultation_id", String.valueOf(questionId))
                .addBodyParameter("user_id", String.valueOf(SharedPrefManager.getInstance(this).getUserId()))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        JSONObject obj = response;
                        try {

                            Toast.makeText(PsychologistReplyViewActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();

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
}