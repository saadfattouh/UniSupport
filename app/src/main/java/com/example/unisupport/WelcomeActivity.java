package com.example.unisupport;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class WelcomeActivity extends AppCompatActivity {

    Button mStartAppBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mStartAppBtn = findViewById(R.id.go_to_app_btn);

        mStartAppBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
        });



    }
}