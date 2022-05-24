package com.example.unisupport.student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.unisupport.R;
import com.example.unisupport.api.Urls;
import com.example.unisupport.model.Psychologist;
import com.example.unisupport.model.Student;
import com.example.unisupport.student.adapters.PsychologistsAdapter;
import com.example.unisupport.student.adapters.StudentsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StudentsActivity extends AppCompatActivity {

    ArrayList<Student> list;
    RecyclerView mList;
    StudentsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students);

        mList = findViewById(R.id.rv);


    }


    @Override
    protected void onResume() {
        super.onResume();
        getAllStudents();
    }

    void getAllStudents(){
        String url = Urls.BASE_URL + Urls.GET_ALL_STUDENTS;

        list = new ArrayList<>();

        AndroidNetworking.get(url)
                .setPriority(Priority.MEDIUM)
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
                                             Toast.makeText(StudentsActivity.this, message, Toast.LENGTH_SHORT).show();
                                             //getting the user from the response
                                             JSONArray array = obj.getJSONArray("data");
                                             Student user;
                                             for (int i = 0; i < array.length(); i++){
                                                 JSONObject item = array.getJSONObject(i);
                                                 user = new Student();
                                                 user.setId(item.getInt("id"));
                                                 user.setName(item.getString("first_name") + item.getString("last_name"));
                                                 user.setImage(Urls.UPDATE_PROFILE + "profile_photo_url");

                                                 list.add(user);
                                             }
                                             mAdapter = new StudentsAdapter(StudentsActivity.this, list);
                                             mList.setAdapter(mAdapter);
                                         } catch (JSONException e) {
                                             e.printStackTrace();
                                         }
                                     }
                                     @Override
                                     public void onError(ANError error) {
                                         // handle error
                                         Toast.makeText(StudentsActivity.this, error.getErrorBody(), Toast.LENGTH_SHORT).show();
                                     }
                                 }
                );
    }
}