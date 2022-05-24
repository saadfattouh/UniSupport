package com.example.unisupport.student.fragments.administrative_childs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.unisupport.Constants;
import com.example.unisupport.LoginActivity;
import com.example.unisupport.R;
import com.example.unisupport.api.Urls;
import com.example.unisupport.employee.MainActivityEmployee;
import com.example.unisupport.model.User;
import com.example.unisupport.psychologist.MainActivityPsychologist;
import com.example.unisupport.student.MainActivityStudent;
import com.example.unisupport.utils.SharedPrefManager;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;


public class AdministrativeConsultationFragment extends Fragment {

    Context context;

    TextInputEditText mDescription;
    Button mSubmitBtn;

    public AdministrativeConsultationFragment() {
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
        return inflater.inflate(R.layout.fragment_administrative_consultation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSubmitBtn = view.findViewById(R.id.submit_btn);
        mDescription = view.findViewById(R.id.description);

        mSubmitBtn.setOnClickListener(v -> {
            String description = mDescription.getText().toString();
            if(description.isEmpty()){
                Toast.makeText(context, "You can't send an empty question!", Toast.LENGTH_SHORT).show();
            }else {
                submitQuery(description);
            }
        });
    }

    private void submitQuery(String description) {
        ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        String url = Urls.BASE_URL + Urls.ASK_EMPLOYEES;

        AndroidNetworking.post(url)
                .addBodyParameter("student_id", String.valueOf(SharedPrefManager.getInstance(context).getUserId()))
                .addBodyParameter("description", mDescription.getText().toString())
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();

                        try {
                            String message = response.getString("message");

                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            Navigation.findNavController(requireView()).navigate(R.id.student_community);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        pDialog.dismiss();
                        Toast.makeText(context, error.getErrorBody(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}