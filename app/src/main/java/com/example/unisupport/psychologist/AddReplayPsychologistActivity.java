package com.example.unisupport.psychologist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.unisupport.R;
import com.example.unisupport.api.Urls;
import com.example.unisupport.employee.AddReplyActivity;
import com.example.unisupport.utils.SharedPrefManager;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class AddReplayPsychologistActivity extends AppCompatActivity {

    Button mSaveBtn;
    TextInputEditText mContent;

    int consultation_id;
    String question;
    int student_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_replay_psychologist);

        Intent sender = getIntent();
        if(sender != null){
            consultation_id = sender.getIntExtra("consultation_id", -1);
            question = sender.getStringExtra("question");
            student_id = sender.getIntExtra("student_id", -1);
        }

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

        String url = Urls.BASE_URL + Urls.PSYCHOLOGIST_REPLY_TO_STUDENTS_CONSULTATIONS;

        AndroidNetworking.post(url)
                .addBodyParameter("consultation_id", String.valueOf(consultation_id))
                .addBodyParameter("student_id", String.valueOf(student_id))
                .addBodyParameter("replay", reply)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();

                        try {
                            String message = response.getString("message");
                            Toast.makeText(AddReplayPsychologistActivity.this, message, Toast.LENGTH_SHORT).show();
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        pDialog.dismiss();
                        Toast.makeText(AddReplayPsychologistActivity.this, error.getErrorBody(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}