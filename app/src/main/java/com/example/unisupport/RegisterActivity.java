package com.example.unisupport;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText mStIDEt, mFNameEt, mLNameEt, mEmailEt, mPhoneEt, mPasswordEt, mJobCareesEt;
    LinearLayout mStudent, mPsychologist, mEmployee;
    ImageView mUploadBtn;
    RadioGroup mAccountTypeSelector;
    Button mRegisterBtn, mToLoginBtn;

    int selectedType = Constants.TYPE_STUDENT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        bindViews();

        mAccountTypeSelector.check(R.id.student);

        updateUI();

        mAccountTypeSelector.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId){
                case R.id.student:
                    selectedType = Constants.TYPE_STUDENT;
                    updateUI();
                    break;
                case R.id.psychologist:
                    selectedType = Constants.TYPE_PSYCHOLOGIST;
                    updateUI();
                    break;
                case R.id.employee:
                    selectedType = Constants.TYPE_EMPLOYEE;
                    updateUI();
                    break;
            }
        });

        mRegisterBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, VerifyAccountActivity.class));
        });


    }

    private void bindViews() {
        mStIDEt = findViewById(R.id.st_id);
        mFNameEt = findViewById(R.id.first_name);
        mLNameEt = findViewById(R.id.last_name);
        mEmailEt = findViewById(R.id.email);
        mPhoneEt = findViewById(R.id.phone);
        mPasswordEt = findViewById(R.id.password);
        mJobCareesEt = findViewById(R.id.job_career);
        mUploadBtn = findViewById(R.id.upload_btn);

        mAccountTypeSelector = findViewById(R.id.type_chooser);

        mRegisterBtn = findViewById(R.id.signup_btn);
        mToLoginBtn =findViewById(R.id.login_btn);

        mStudent = findViewById(R.id.student_id_layout);
        mPsychologist = findViewById(R.id.psychologist_upload_layout);
        mEmployee = findViewById(R.id.employee_career_layout);
    }

    private void updateUI() {
        switch (selectedType){
            case Constants.TYPE_STUDENT:
                mStudent.setVisibility(View.VISIBLE);
                mEmployee.setVisibility(View.GONE);
                mPsychologist.setVisibility(View.GONE);
                break;
            case Constants.TYPE_PSYCHOLOGIST:
                mStudent.setVisibility(View.GONE);
                mEmployee.setVisibility(View.GONE);
                mPsychologist.setVisibility(View.VISIBLE);
                break;
            case Constants.TYPE_EMPLOYEE:
                mStudent.setVisibility(View.GONE);
                mEmployee.setVisibility(View.VISIBLE);
                mPsychologist.setVisibility(View.GONE);
                break;

        }
    }


}