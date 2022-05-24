package com.example.unisupport.employee.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.unisupport.R;
import com.example.unisupport.api.Urls;
import com.example.unisupport.employee.AddPageActivity;
import com.example.unisupport.model.Page;
import com.example.unisupport.model.Post;
import com.example.unisupport.student.adapters.PagesAdapter;
import com.example.unisupport.student.adapters.PostsAdapter;
import com.example.unisupport.utils.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class PagesFragment extends Fragment {

    SearchView mSearch;

    RecyclerView mList;
    PagesAdapter mPagesAdapter;
    ArrayList<Page> pages;

    Context context;

    public PagesFragment() {
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
        return inflater.inflate(R.layout.fragment_pages, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mList = view.findViewById(R.id.rv);
        mSearch = view.findViewById(R.id.search_view);

        view.findViewById(R.id.add_page_btn).setOnClickListener(v -> {
            startActivity(new Intent(context, AddPageActivity.class));
        });

        mSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mPagesAdapter.getFilter().filter(newText);
                return true;
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        getPages();
    }

    public void getPages(){

        ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setCancelable(false);
        pDialog.setMessage("Processing please wait...");
        pDialog.show();

        String url = Urls.BASE_URL + Urls.GET_ALL_PAGES;

        pages = new ArrayList<>();

        AndroidNetworking.get(url)
                .setPriority(Priority.MEDIUM)
                .addQueryParameter("user_id", String.valueOf(SharedPrefManager.getInstance(context).getUserId()))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                                     @Override
                                     public void onResponse(JSONObject response) {
                                         pDialog.dismiss();
                                         // do anything with response
                                         try {
                                             //converting response to json object
                                             JSONObject obj = response;
                                             String message = response.getString("message");

                                             //if no error in response
                                             Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                             //getting the user from the response
                                             JSONArray array = obj.getJSONArray("data");
                                             Page page;
                                             for (int i = 0; i < array.length(); i++) {
                                                 JSONObject item = array.getJSONObject(i);
                                                 page = new Page();
                                                 page.setId(item.getInt("id"));
                                                 page.setContent(item.getString("content_URL"));
                                                 page.setUrl(item.getString("page_URL"));
                                                 page.setDate(item.getString("date").split("T")[0]);
                                                 JSONObject admin = item.getJSONObject("admin");
                                                 page.setAdmin_id(admin.getInt("id"));
                                                 page.setAdmin_name(admin.getString("first_name") + " " + admin.getString("last_name"));

                                                 pages.add(page);
                                             }
                                             mPagesAdapter = new PagesAdapter(context, pages);
                                             mList.setAdapter(mPagesAdapter);
                                         } catch (JSONException e) {
                                             e.printStackTrace();
                                         }
                                     }
                                     @Override
                                     public void onError(ANError error) {
                                         pDialog.dismiss();
                                         // handle error
                                         Toast.makeText(context, error.getErrorBody(), Toast.LENGTH_SHORT).show();
                                     }
                                 }
                );
    }
}