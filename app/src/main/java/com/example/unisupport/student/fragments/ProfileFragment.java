package com.example.unisupport.student.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.example.unisupport.Constants;
import com.example.unisupport.R;
import com.example.unisupport.RegisterActivity;
import com.example.unisupport.api.Urls;
import com.example.unisupport.model.User;
import com.example.unisupport.student.MainActivityStudent;
import com.example.unisupport.utils.FileUtils;
import com.example.unisupport.utils.SharedPrefManager;
import com.example.unisupport.utils.Validation;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;


public class ProfileFragment extends Fragment {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 100;
    private static final int PICK_IMAGE_REQUEST = 200;

    TextInputEditText mFNameEt, mLNameEt, mPhoneEt, mBio;

    private Uri profileImageUri;
    private String profileImagePath;

    ImageView mAddProfileImage;

    Button mUpdateProfileBtn;

    Context context;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bindViews(view);

        updateUI();

        view.findViewById(R.id.upload_image_btn).setOnClickListener(v -> {
            requestRead();
        });

        mUpdateProfileBtn.setOnClickListener(v -> {
            if(Validation.validateInput(context, mFNameEt, mLNameEt, mPhoneEt)){
                updateUserData();
            }
        });

    }

    private void updateUserData() {
        ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        String url = Urls.BASE_URL + Urls.UPDATE_PROFILE;

        ANRequest.MultiPartBuilder androidNetworking = AndroidNetworking.upload(url);

        if(profileImagePath != null)
            androidNetworking.addMultipartFile("profile_photo_url", new File(profileImagePath));

        androidNetworking.addMultipartParameter("first_name",mFNameEt.getText().toString());
        androidNetworking.addMultipartParameter("last_name",mFNameEt.getText().toString());
        androidNetworking.addMultipartParameter("phone", mPhoneEt.getText().toString());

        androidNetworking.setPriority(Priority.HIGH);
        androidNetworking
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
                            data.getString("bio"),
                            null,
                            null,
                            data.getString("profile_photo_url")
                    );

                    SharedPrefManager.getInstance(context).studentLogin(user);
                    Toast.makeText(context, "Updated successfully!", Toast.LENGTH_SHORT).show();
                    updateUI();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void onError(ANError error) {
                pDialog.dismiss();
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateUI() {
        User user = SharedPrefManager.getInstance(context).getUserData();

        mFNameEt.setText(user.getFirstName());
        mLNameEt.setText(user.getLastName());
        mPhoneEt.setText(user.getPhone());
        mBio.setText(user.getBio() == null ? "" : user.getBio());

        Glide.with(context).load(user.getProfileImage()).into(mAddProfileImage);
    }


    private void bindViews(View view) {
        mFNameEt = view.findViewById(R.id.first_name);
        mLNameEt = view.findViewById(R.id.last_name);
        mPhoneEt = view.findViewById(R.id.phone);
        mAddProfileImage = view.findViewById(R.id.profile_image);
        mBio = view.findViewById(R.id.bio);
        mUpdateProfileBtn = view.findViewById(R.id.update_btn);
    }

    //..................Methods for File Chooser.................
    public void requestRead() {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            openFileChooser();
        }
    }

    public void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == requireActivity().RESULT_OK && data != null && data.getData() != null) {
            profileImageUri = data.getData();

            Uri picUri = profileImageUri;
            profileImagePath = FileUtils.getPath(context, picUri);
            if (profileImagePath != null) {
                Glide.with(context).load(profileImageUri).into(mAddProfileImage);
            }
            else
            {
                Toast.makeText(context,"no image selected", Toast.LENGTH_LONG).show();
            }
        }


    }

    //..............................................................................

}