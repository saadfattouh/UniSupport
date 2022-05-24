package com.example.unisupport;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.unisupport.employee.MainActivityEmployee;
import com.example.unisupport.student.MainActivityStudent;
import com.google.android.material.textfield.TextInputEditText;

public class VerifyAccountActivity extends AppCompatActivity {

    TextInputEditText mCodeEt;
    Button mVerifyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifty_account);

        bindViews();

        mVerifyBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivityStudent.class));
        });
    }

    private void bindViews() {
        mCodeEt = findViewById(R.id.code);
        mVerifyBtn = findViewById(R.id.verify_btn);
    }
}