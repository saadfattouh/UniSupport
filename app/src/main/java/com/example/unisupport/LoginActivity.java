package com.example.unisupport;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.unisupport.student.MainActivityStudent;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText mEmailEt, mPasswordEt;
    Button mLoginBtn, mToRegisterEt;

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
            startActivity(new Intent(this, MainActivityStudent.class));
        });
    }

    private void bindViews() {
        mEmailEt = findViewById(R.id.email);
        mPasswordEt = findViewById(R.id.password);
        mLoginBtn = findViewById(R.id.login_btn);
        mToRegisterEt = findViewById(R.id.signup_btn);
    }
}