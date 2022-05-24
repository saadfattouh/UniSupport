package com.example.unisupport.psychologist.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
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
import com.example.unisupport.employee.StudentsConsultationsAdapter;
import com.example.unisupport.model.StudentConsultation;
import com.example.unisupport.model.StudentQuestion;
import com.example.unisupport.psychologist.StudentsQuestionsAdapter;
import com.example.unisupport.utils.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class StudentsQuestionsFragment extends Fragment {

    StudentsQuestionsAdapter mAdapter;
    RecyclerView mList;
    ArrayList<StudentQuestion> replies;

    Context context;


    public StudentsQuestionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_students_questions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mList = view.findViewById(R.id.rv);
    }

    @Override
    public void onResume() {
        super.onResume();
        getAllStudentQuestions();
    }

    void getAllStudentQuestions(){

        String url = Urls.BASE_URL + Urls.GET_ALL_PSYCHOLOGIST_STUDENTS_CONSULTATIONS;

        replies = new ArrayList<>();

        AndroidNetworking.get(url)
                .setPriority(Priority.MEDIUM)
                .addQueryParameter("psychologist_id", String.valueOf(SharedPrefManager.getInstance(context).getUserId()))
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
                                             StudentQuestion reply;
                                             for (int i = 0; i < array.length(); i++){
                                                 JSONObject item = array.getJSONObject(i);
                                                 reply = new StudentQuestion();
                                                 reply.setId(item.getInt("id"));
                                                 reply.setQuestion(item.getString("description"));
                                                 if(!item.isNull("reply"))
                                                     reply.setAnswer(item.getString("reply"));
                                                 reply.setPsychologist_id(item.getString("psychologist_id"));
                                                 reply.setAsker_id(item.getInt("student_id"));
                                                 JSONObject student = item.getJSONObject("student");
                                                 reply.setS_name(student.getString("first_name") + " " + student.getString("last_name"));
                                                 //to distinguish between answered and new answers... here we  need the new answers
                                                 if(reply.getAnswer() == null)
                                                     replies.add(reply);
                                             }
                                             mAdapter = new StudentsQuestionsAdapter(context, replies, Constants.NOT_ANSWERED);
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