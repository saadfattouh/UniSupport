package com.example.unisupport.employee.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.unisupport.Constants;
import com.example.unisupport.R;
import com.example.unisupport.api.Urls;
import com.example.unisupport.model.StudentConsultation;
import com.example.unisupport.employee.StudentsConsultationsAdapter;
import com.example.unisupport.utils.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class RepliesAdministrativeFragment extends Fragment {

    StudentsConsultationsAdapter mAdapter;
    RecyclerView mList;
    ArrayList<StudentConsultation> replies;

    Context context;

    public RepliesAdministrativeFragment() {
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
        return inflater.inflate(R.layout.fragment_replies_administrative, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mList = view.findViewById(R.id.rv);
    }

    @Override
    public void onResume() {
        super.onResume();
        getMyReplies();
    }

    void getMyReplies(){
        String url = Urls.BASE_URL + Urls.GET_ALL_ADMINISTRATIVE_STUDENTS_CONSULTATIONS;

        replies = new ArrayList<>();

        AndroidNetworking.get(url)
                .setPriority(Priority.MEDIUM)
                .addQueryParameter("employee_id", String.valueOf(SharedPrefManager.getInstance(context).getUserId()))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                                     @Override
                                     public void onResponse(JSONObject response) {
                                         // do anything with response
                                         try {
                                             //converting response to json object
                                             JSONObject obj = response;
                                             String message = response.getString("message");

                                             //if no error in response
                                             Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                             //getting the user from the response
                                             JSONArray array = obj.getJSONArray("data");
                                             StudentConsultation reply;
                                             for (int i = 0; i < array.length(); i++){
                                                 JSONObject item = array.getJSONObject(i);
                                                 reply = new StudentConsultation();
                                                 reply.setId(item.getInt("id"));
                                                 reply.setQuestion(item.getString("description"));
                                                 if(!item.isNull("reply"))
                                                    reply.setAnswer(item.getString("reply"));
                                                 if(!item.isNull("employee_id"))
                                                    reply.setAdmin_id(item.getString("employee_id"));
                                                 reply.setAsker_id(item.getInt("student_id"));
                                                 JSONObject student = item.getJSONObject("student");
                                                 reply.setS_name(student.getString("first_name") + " " + student.getString("last_name"));

                                                 if(reply.getAnswer() != null)
                                                     replies.add(reply);
                                             }
                                             mAdapter = new StudentsConsultationsAdapter(context, replies, Constants.ANSWERED);
                                             mList.setAdapter(mAdapter);
                                         } catch (JSONException e) {
                                             e.printStackTrace();
                                         }
                                     }
                                     @Override
                                     public void onError(ANError error) {
                                         // handle error
                                         Toast.makeText(context, error.getErrorBody(), Toast.LENGTH_SHORT).show();
                                     }
                                 }
                );
    }
}