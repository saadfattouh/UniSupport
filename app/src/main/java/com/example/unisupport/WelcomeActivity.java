package com.example.unisupport;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.unisupport.employee.MainActivityEmployee;
import com.example.unisupport.psychologist.MainActivityPsychologist;
import com.example.unisupport.student.MainActivityStudent;
import com.example.unisupport.utils.SharedPrefManager;

public class WelcomeActivity extends AppCompatActivity {

    Button mStartAppBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mStartAppBtn = findViewById(R.id.go_to_app_btn);

        mStartAppBtn.setOnClickListener(v -> {
            if(SharedPrefManager.getInstance(this).isLoggedIn()){
                switch (SharedPrefManager.getInstance(this).getUserType()){
                    case Constants.TYPE_STUDENT:
                        startActivity(new Intent(this, MainActivityStudent.class));
                        break;
                    case Constants.TYPE_PSYCHOLOGIST:
                        startActivity(new Intent(this, MainActivityPsychologist.class));
                        break;
                    case Constants.TYPE_EMPLOYEE:
                        startActivity(new Intent(this, MainActivityEmployee.class));
                        break;
                }
            }else
                startActivity(new Intent(this, LoginActivity.class));
        });




    }
}