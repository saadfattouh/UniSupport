package com.example.unisupport.student.fragments;

import android.Manifest;
import android.app.Dialog;
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
import androidx.navigation.Navigation;

import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.unisupport.R;
import com.example.unisupport.api.Urls;
import com.example.unisupport.student.PsychologistReplyViewActivity;
import com.example.unisupport.utils.SharedPrefManager;
import com.example.unisupport.utils.Validation;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import static android.app.Activity.RESULT_OK;

public class AddNewPostFragment extends Fragment {
    
    TextInputEditText mTitleEt, mSpecializationEt, mDescEt;
    TextInputEditText mSuggestToolEt, mToolLinkEt;

    ImageView uploadBtn;

    Button mPublishBtn;

    Context context;

    private static final int PICK_FILE_REQUEST = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 101;
    Uri fileUri;
    String filePath = null;
    File pdfFile;


    public AddNewPostFragment() {
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
        return inflater.inflate(R.layout.fragment_add_new_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTitleEt = view.findViewById(R.id.title);
        mDescEt = view.findViewById(R.id.description);
        mSuggestToolEt = view.findViewById(R.id.tool);
        mToolLinkEt = view.findViewById(R.id.tool_link);
        mSpecializationEt = view.findViewById(R.id.specialization);
        uploadBtn = view.findViewById(R.id.upload_btn);
        mPublishBtn = view.findViewById(R.id.publish_btn);


        uploadBtn.setOnClickListener(v -> {
            requestRead();
        });

        mPublishBtn.setOnClickListener(v -> {
            if(Validation.validateInput(context, mTitleEt, mSpecializationEt, mDescEt, mSuggestToolEt, mToolLinkEt))
                publishPost();
        });


    }

    private void publishPost() {

        ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Processing please wait...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();

        String url = Urls.BASE_URL + Urls.ADD_NEW_POST;

        AndroidNetworking.post(url)
                .setPriority(Priority.MEDIUM)
                .addBodyParameter("user_id", String.valueOf(SharedPrefManager.getInstance(context).getUserId()))
                .addBodyParameter("title", mTitleEt.getText().toString().trim())
                .addBodyParameter("specification", mSpecializationEt.getText().toString().trim())
                .addBodyParameter("description", mDescEt.getText().toString().trim())
                .addBodyParameter("tool_name", mSuggestToolEt.getText().toString().trim())
                .addBodyParameter("tool_link", mToolLinkEt.getText().toString().trim())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        // do anything with response
                        JSONObject obj = response;
                        try {

                            Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            Navigation.findNavController(requireView()).popBackStack();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        pDialog.dismiss();
                        // handle error
                        Log.e("Error", error.getErrorBody());

                    }
                });
    }

    //..................Methods for File Chooser.................
    public void requestRead() {
        if (ContextCompat.checkSelfPermission(requireContext(),
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
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("*/*");
        chooseFile = Intent.createChooser(chooseFile, "Choose a .pdf file");
        startActivityForResult(chooseFile, PICK_FILE_REQUEST);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            fileUri = data.getData();

            filePath = getPath(fileUri);
            if (filePath != null) {
                pdfFile = new File(filePath);
//                Log.d("filePath", String.valueOf(filePath));
            }
            else
            {
                Toast.makeText(getContext(),"no file selected", Toast.LENGTH_LONG).show();
            }

            Toast.makeText(getContext(), "done successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    public String getPath(Uri uri) {

        String path = null;
        String[] projection = { MediaStore.Files.FileColumns.DATA };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);

        if(cursor == null){
            path = uri.getPath();
        }
        else{
            cursor.moveToFirst();
            int column_index = cursor.getColumnIndexOrThrow(projection[0]);
            path = cursor.getString(column_index);
            cursor.close();
        }

        return ((path == null || path.isEmpty()) ? (uri.getPath()) : path);
    }
    //..............................................................................
}