package com.example.unisupport.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.contentcapture.ContentCaptureSession;

import com.example.unisupport.Constants;
import com.example.unisupport.model.User;


public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "generalFile";

    private static final String KEY_ID = "key_id";
    private static final String KEY_FIRST_NAME = "key_f_name";
    private static final String KEY_LAST_NAME = "key_l_name";
    private static final String KEY_EMAIL = "key_email";
    private static final String KEY_PHONE = "key_phone";
    private static final String KEY_BIO = "key_bio";
    private static final String KEY_PROFILE_IMAGE = "key_profile_image";
    private static final String KEY_LICENSE_IMAGE = "key_license_image";
    private static final String KEY_JOB_CAREER = "key_job_career";
    private static final String KEY_USER_TYPE = "key)user_type";


    private static SharedPrefManager mInstance;
    private static Context context;

    private SharedPrefManager(Context context) {
        SharedPrefManager.context = context;
    }
    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //this method will store the user data in shared preferences
    //student
    public void studentLogin(User user) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, user.getId());
        editor.putString(KEY_FIRST_NAME, user.getFirstName());
        editor.putString(KEY_LAST_NAME, user.getLastName());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_PHONE, user.getPhone());
        editor.putString(KEY_PROFILE_IMAGE, user.getProfileImage());
        editor.putInt(KEY_USER_TYPE, Constants.TYPE_STUDENT);
        editor.apply();
    }

    //employee
    public void employeeLogin(User user) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, user.getId());
        editor.putString(KEY_FIRST_NAME, user.getFirstName());
        editor.putString(KEY_LAST_NAME, user.getLastName());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_PHONE, user.getPhone());
        editor.putString(KEY_PROFILE_IMAGE, user.getProfileImage());
        editor.putString(KEY_JOB_CAREER, user.getJobCareer());
        editor.putInt(KEY_USER_TYPE, Constants.TYPE_EMPLOYEE);
        editor.apply();
    }

    //psychologist
    public void psychologistLogin(User user) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, user.getId());
        editor.putString(KEY_FIRST_NAME, user.getFirstName());
        editor.putString(KEY_LAST_NAME, user.getLastName());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_PHONE, user.getPhone());
        editor.putString(KEY_PROFILE_IMAGE, user.getProfileImage());
        editor.putString(KEY_LICENSE_IMAGE, user.getLicensePhoto());
        editor.putInt(KEY_USER_TYPE, Constants.TYPE_PSYCHOLOGIST);
        editor.apply();
    }

    //this method will check whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_ID, -1) != -1;
    }


    //this method will give the logged in user id
    public int getUserId() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_ID, -1);
    }

    public int getUserType() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_USER_TYPE, -1);
    }

    public String  getStudentEmail() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_EMAIL, null);
    }



    //this method will give the logged in user
    public User getUserData() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_FIRST_NAME, null),
                sharedPreferences.getString(KEY_LAST_NAME, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getString(KEY_PHONE, null),
                sharedPreferences.getString(KEY_BIO, null),
                sharedPreferences.getString(KEY_JOB_CAREER, null),
                sharedPreferences.getString(KEY_LICENSE_IMAGE, null),
                sharedPreferences.getString(KEY_PROFILE_IMAGE, null)
                );
    }


    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().commit();
    }



}
