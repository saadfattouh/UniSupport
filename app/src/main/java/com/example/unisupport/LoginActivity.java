package com.example.unisupport;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.unisupport.api.Urls;
import com.example.unisupport.employee.MainActivityEmployee;
import com.example.unisupport.model.User;
import com.example.unisupport.psychologist.MainActivityPsychologist;
import com.example.unisupport.student.MainActivityStudent;
import com.example.unisupport.utils.SharedPrefManager;
import com.example.unisupport.utils.Validation;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText mEmailEt, mPasswordEt;
    Button mLoginBtn, mToRegisterEt;

    Intent wayToGo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        bindViews();

        mToRegisterEt.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
            finish();
        });

        mLoginBtn.setOnClickListener(v -> {
            if(Validation.validateInput(this, mEmailEt, mPasswordEt)){
                loginUser();
            }
        });
    }

    //"User not found!" if email or password are wrong
    private void loginUser() {
        if(Validation.validateInput(this, mEmailEt, mPasswordEt)){
            ProgressDialog pDialog = new ProgressDialog(this);
            pDialog.setMessage("Loading please wait...");
            pDialog.setCancelable(false);
            pDialog.show();


            String url = Urls.BASE_URL + Urls.LOGIN;

            AndroidNetworking.post(url)
                    .addBodyParameter("email", mEmailEt.getText().toString())
                    .addBodyParameter("password", mPasswordEt.getText().toString())
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            pDialog.dismiss();

                            try {
                                String message = response.getString("message");
                                if(message.equals("User founded!")){
                                    JSONObject data = response.getJSONObject("data");
                                    int userType = Integer.parseInt(data.getString("type"));
                                    User user;
                                    switch (userType){
                                        case Constants.TYPE_STUDENT:
                                            user = new User(
                                                    Integer.parseInt(data.getString("id")),
                                                    data.getString("first_name"),
                                                    data.getString("last_name"),
                                                    data.getString("email"),
                                                    data.getString("phone"),
                                                    null,
                                                    null,
                                                    null,
                                                    data.getString("profile_photo_url")
                                            );

                                            SharedPrefManager.getInstance(LoginActivity.this).studentLogin(user);
                                            wayToGo = new Intent(LoginActivity.this, MainActivityStudent.class);
                                            break;
                                        case Constants.TYPE_EMPLOYEE:
                                            user = new User(
                                                    Integer.parseInt(data.getString("id")),
                                                    data.getString("first_name"),
                                                    data.getString("last_name"),
                                                    data.getString("email"),
                                                    data.getString("phone"),
                                                    null,
                                                    data.getString("job_career"),
                                                    null,
                                                    data.getString("profile_photo_url")
                                            );

                                            SharedPrefManager.getInstance(LoginActivity.this).employeeLogin(user);
                                            wayToGo = new Intent(LoginActivity.this, MainActivityEmployee.class);
                                            break;
                                        case Constants.TYPE_PSYCHOLOGIST:
                                            user = new User(
                                                    Integer.parseInt(data.getString("id")),
                                                    data.getString("first_name"),
                                                    data.getString("last_name"),
                                                    data.getString("email"),
                                                    data.getString("phone"),
                                                    null,
                                                    null,
                                                    data.getString("license_photo"),
                                                    data.getString("profile_photo_url")
                                            );

                                            SharedPrefManager.getInstance(LoginActivity.this).psychologistLogin(user);
                                            wayToGo = new Intent(LoginActivity.this, MainActivityPsychologist.class);
                                            break;
                                    }

                                    //done setting up now go to the preferred destination
                                    startActivity(wayToGo);
                                    finish();

                                }else if(message.equals("User not found!")){
                                    Toast.makeText(LoginActivity.this, "Wrong email or password", Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        @Override
                        public void onError(ANError error) {
                            pDialog.dismiss();
                            Toast.makeText(LoginActivity.this, error.getErrorBody(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }

    private void bindViews() {
        mEmailEt = findViewById(R.id.email);
        mPasswordEt = findViewById(R.id.password);
        mLoginBtn = findViewById(R.id.login_btn);
        mToRegisterEt = findViewById(R.id.signup_btn);
    }


}