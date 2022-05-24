package com.example.unisupport.student.fragments.psychologists_childs;

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
import com.example.unisupport.R;
import com.example.unisupport.api.Urls;
import com.example.unisupport.model.AdministrativeReply;
import com.example.unisupport.model.PsychologistReply;
import com.example.unisupport.student.adapters.AdministrativesAdapter;
import com.example.unisupport.student.adapters.PsychologistsRepliesAdapter;
import com.example.unisupport.utils.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class PsychologistsRepliesFragment extends Fragment {

    PsychologistsRepliesAdapter mAdapter;
    RecyclerView mList;
    ArrayList<PsychologistReply> replies;

    Context context;

    public PsychologistsRepliesFragment() {
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
        return inflater.inflate(R.layout.fragment_psychologists_replies, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mList = view.findViewById(R.id.rv);

    }

    @Override
    public void onResume() {
        super.onResume();
        getRepliesByPsychologistAndStudentId(SharedPrefManager.getInstance(context).getUserId());
    }

    void getRepliesByPsychologistAndStudentId(int student_id){
        String url = Urls.BASE_URL + Urls.GET_STUDENT_PSYCHOLOGISTS_QUESTIONS;

        replies = new ArrayList<>();

        AndroidNetworking.get(url)
                .setPriority(Priority.MEDIUM)
                .addQueryParameter("student_id", String.valueOf(student_id))
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
                                             PsychologistReply reply;
                                             for (int i = 0; i < array.length(); i++){
                                                 JSONObject item = array.getJSONObject(i);
                                                 reply = new PsychologistReply();
                                                 reply.setId(item.getInt("id"));
                                                 reply.setQuestion(item.getString("description"));
                                                 if(!item.isNull("reply"))
                                                    reply.setAnswer(item.getString("reply"));
                                                 reply.setAsker_id(item.getInt("student_id"));
                                                 if(reply.getAnswer() != null){
                                                     reply.setPsychologist_id(item.getInt("psychologist_id"));
                                                     JSONObject employee = item.getJSONObject("psychologist");
                                                     reply.setP_name(employee.getString("first_name") + " " + employee.getString("last_name"));

                                                     replies.add(reply);
                                                 }
                                             }
                                             mAdapter = new PsychologistsRepliesAdapter(context, replies);
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