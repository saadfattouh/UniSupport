package com.example.unisupport;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.bumptech.glide.Glide;
import com.example.unisupport.api.Urls;
import com.example.unisupport.employee.MainActivityEmployee;
import com.example.unisupport.model.User;
import com.example.unisupport.psychologist.MainActivityPsychologist;
import com.example.unisupport.student.MainActivityStudent;
import com.example.unisupport.utils.FileUtils;
import com.example.unisupport.utils.SharedPrefManager;
import com.example.unisupport.utils.Validation;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class RegisterActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 100;
    private static final int PICK_IMAGE_REQUEST = 200;
    private static final int PICK_LICENSE_REQUEST = 300;

    TextInputEditText mStIDEt, mFNameEt, mLNameEt, mEmailEt, mPhoneEt, mPasswordEt, mPasswordConfirm, mJobCareesEt;
    LinearLayout mStudent, mPsychologist, mEmployee;

    ImageView mUploadBtn, mAddProfileImage;

    RadioGroup mAccountTypeSelector;
    Button mRegisterBtn, mToLoginBtn;

    int selectedType = Constants.TYPE_STUDENT;

    private Uri psychologistLicenseUri, profileImageUri;
    private String psychologistLicensePath, profileImagePath;

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
            switch (selectedType){
                case Constants.TYPE_STUDENT:
                    registerStudent();
                    break;
                case Constants.TYPE_PSYCHOLOGIST:
                    registerPsychologist();
                    break;
                case Constants.TYPE_EMPLOYEE:
                    registerEmployee();
                    break;
            }
        });

        mUploadBtn.setOnClickListener(v -> {
            requestRead(PICK_LICENSE_REQUEST);
        });

        mAddProfileImage.setOnClickListener(v -> {
            requestRead(PICK_IMAGE_REQUEST);
        });


    }

    private void registerEmployee() {
        //first validate input
        if(Validation.validateInput(this, mFNameEt, mLNameEt, mEmailEt, mPhoneEt, mPasswordEt, mJobCareesEt)) {
            if(mEmailEt.getText().toString().matches("^[\\w]+[\\w.%+-]*@su\\.edu\\.sa$")){
                if (profileImagePath != null) {
                    ProgressDialog pDialog = new ProgressDialog(this);
                    pDialog.setMessage("Loading please wait...");
                    pDialog.setCancelable(false);
                    pDialog.show();

                    String url = Urls.BASE_URL + Urls.REGISTER;

                    AndroidNetworking.upload(url)
                            .addMultipartFile("profile_photo_url",new File(profileImagePath))
                            .addMultipartParameter("first_name",mFNameEt.getText().toString())
                            .addMultipartParameter("last_name", mLNameEt.getText().toString())
                            .addMultipartParameter("email", mEmailEt.getText().toString())
                            .addMultipartParameter("type", String.valueOf(Constants.TYPE_EMPLOYEE))
                            .addMultipartParameter("phone", mPhoneEt.getText().toString())
                            .addMultipartParameter("password", mPasswordEt.getText().toString())
                            .addMultipartParameter("job_career", mJobCareesEt.getText().toString())
                            .setPriority(Priority.HIGH)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    pDialog.dismiss();

                                    try {
                                        JSONObject data = response.getJSONObject("data");
                                        User user = new User(
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

                                        SharedPrefManager.getInstance(RegisterActivity.this).employeeLogin(user);
                                        startActivity(new Intent(RegisterActivity.this, MainActivityEmployee.class));
                                        Toast.makeText(RegisterActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                                        finish();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                                @Override
                                public void onError(ANError error) {
                                    pDialog.dismiss();
                                    try {
                                        JSONObject errorMessage = new JSONObject(error.getErrorBody());
                                        JSONObject missing = errorMessage.getJSONObject("data");
                                        if(missing.has("email")){
                                            mEmailEt.setError("email is already existed!");
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Toast.makeText(RegisterActivity.this, error.getErrorBody(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(this, "profile image is required!", Toast.LENGTH_SHORT).show();
                }
            }else {
                mEmailEt.setError("you need a valid Shaqraa university employee email!");
            }

        }
    }

    private void registerPsychologist() {
        if(Validation.validateInput(this, mFNameEt, mLNameEt, mEmailEt, mPhoneEt, mPasswordEt)){

            if(profileImagePath == null){
                Toast.makeText(this, "profile image is required!", Toast.LENGTH_SHORT).show();
                return;
            }

            if(psychologistLicensePath == null){
                Toast.makeText(this, "your license is required!", Toast.LENGTH_SHORT).show();
                return;
            }

            ProgressDialog pDialog = new ProgressDialog(this);
            pDialog.setMessage("Loading please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

            String url = Urls.BASE_URL + Urls.REGISTER;

            AndroidNetworking.upload(url)
                .addMultipartFile("profile_photo_url", new File(profileImagePath))
                .addMultipartParameter("first_name", mFNameEt.getText().toString())
                .addMultipartParameter("last_name", mLNameEt.getText().toString())
                .addMultipartParameter("email", mEmailEt.getText().toString())
                .addMultipartParameter("type", String.valueOf(Constants.TYPE_PSYCHOLOGIST))
                .addMultipartParameter("phone", mPhoneEt.getText().toString())
                .addMultipartParameter("password", mPasswordEt.getText().toString())
                .addMultipartFile("license_photo", new File(psychologistLicensePath))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        try {
                            JSONObject data = response.getJSONObject("data");
                            User user = new User(
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

                            SharedPrefManager.getInstance(RegisterActivity.this).psychologistLogin(user);
                            startActivity(new Intent(RegisterActivity.this, MainActivityPsychologist.class));
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        pDialog.dismiss();
                        try {
                            JSONObject errorMessage = new JSONObject(error.getErrorBody());
                            JSONObject missing = errorMessage.getJSONObject("data");
                            if(missing.has("email")){
                                mEmailEt.setError("email is already existed!");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(RegisterActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            );

        }
    }


    private void registerStudent() {
        //first validate input
        if(Validation.validateInput(this, mStIDEt, mFNameEt, mLNameEt, mEmailEt, mPhoneEt, mPasswordEt)){
            if(profileImagePath != null){
                if(checkPassword() && checkPhone()){
                    ProgressDialog pDialog = new ProgressDialog(this);
                    pDialog.setMessage("Loading please wait...");
                    pDialog.setCancelable(false);
                    pDialog.show();

                    String url = Urls.BASE_URL + Urls.REGISTER;

                    AndroidNetworking.upload(url)
                            .addMultipartFile("profile_photo_url",new File(profileImagePath))
                            .addMultipartParameter("first_name",mFNameEt.getText().toString())
                            .addMultipartParameter("last_name", mLNameEt.getText().toString())
                            .addMultipartParameter("email", mEmailEt.getText().toString())
                            .addMultipartParameter("password", mPasswordEt.getText().toString())
                            .addMultipartParameter("type", String.valueOf(Constants.TYPE_STUDENT))
                            .addMultipartParameter("phone", mPhoneEt.getText().toString())
                            .setPriority(Priority.HIGH)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    pDialog.dismiss();

                                    try {
                                        JSONObject data = response.getJSONObject("data");
                                        User user = new User(
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

                                        SharedPrefManager.getInstance(RegisterActivity.this).studentLogin(user);
                                        startActivity(new Intent(RegisterActivity.this, MainActivityStudent.class));
                                        finish();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                                @Override
                                public void onError(ANError error) {
                                    pDialog.dismiss();
                                    try {
                                        JSONObject errorMessage = new JSONObject(error.getErrorBody());
                                        JSONObject missing = errorMessage.getJSONObject("data");
                                        if(errorMessage.has("email")){
                                            mEmailEt.setError("email is already existed!");
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Toast.makeText(RegisterActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }else {
                Toast.makeText(this, "profile image is required!", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private boolean checkPhone() {
        String phone = mPhoneEt.getText().toString();
        if(phone.length() == 10 && phone.matches("^05[0-9]+$"))
            return true;
        else
            mPhoneEt.setError("invalid phone number!");
        return false;
    }

    private boolean checkPassword() {
        String pass = mPasswordEt.getText().toString();
        if(pass.length() >= 8){
            if(mPasswordConfirm.getText().toString().equals(pass)){
                return true;
            }else {
                mPasswordConfirm.setError("passwords doesn't match!");
            }
        }
        else
            mPasswordEt.setError("password must be at least 8 characters!");
        return false;
    }

    private void bindViews() {
        mStIDEt = findViewById(R.id.st_id);
        mFNameEt = findViewById(R.id.first_name);
        mLNameEt = findViewById(R.id.last_name);
        mEmailEt = findViewById(R.id.email);
        mPhoneEt = findViewById(R.id.phone);
        mPasswordEt = findViewById(R.id.password);
        mPasswordConfirm = findViewById(R.id.password_retype);
        mJobCareesEt = findViewById(R.id.job_career);
        mUploadBtn = findViewById(R.id.upload_btn);
        mAddProfileImage = findViewById(R.id.upload_image_btn);

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


    //..................Methods for File Chooser.................
    public void requestRead(int requestCode) {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            openFileChooser(requestCode);
        }
    }

    public void openFileChooser(int requestCode) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, requestCode);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            profileImageUri = data.getData();

            Uri picUri = profileImageUri;
            profileImagePath = FileUtils.getPath(this, picUri);
            if (profileImagePath != null) {

            }
            else
            {
                Toast.makeText(this,"no image selected", Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == PICK_LICENSE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            psychologistLicenseUri = data.getData();

            Uri picUri = psychologistLicenseUri;
            psychologistLicensePath = FileUtils.getPath(this, picUri);
            if (psychologistLicensePath != null) {

            }
            else
            {
                Toast.makeText(this,"no image selected", Toast.LENGTH_LONG).show();
            }
        }

    }
    //..............................................................................


}