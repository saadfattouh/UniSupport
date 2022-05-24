package com.example.unisupport.student.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.unisupport.Constants;
import com.example.unisupport.R;
import com.example.unisupport.api.Urls;
import com.example.unisupport.model.Page;
import com.example.unisupport.model.Post;
import com.example.unisupport.model.PsychologistReply;
import com.example.unisupport.student.adapters.PagesAdapter;
import com.example.unisupport.student.adapters.PostsAdapter;
import com.example.unisupport.student.adapters.PsychologistsRepliesAdapter;
import com.example.unisupport.utils.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CommunityFragment extends Fragment {

    SearchView mSearch;
    TextView mAddPost;

    RecyclerView mList;

    PostsAdapter mPostsAdapter;
    ArrayList<Post> posts;

    PagesAdapter mPagesAdapter;
    ArrayList<Page> pages;

    RadioGroup contentTypeSelector;

    Context context;

    NavController navController;

    public CommunityFragment() {
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
        return inflater.inflate(R.layout.fragment_community, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAddPost = view.findViewById(R.id.add_post);
        mSearch = view.findViewById(R.id.search_view);
        mList = view.findViewById(R.id.rv);
        contentTypeSelector = view.findViewById(R.id.content_type_selector);

        mAddPost.setOnClickListener(v -> {
            navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_student_community_to_addNewPostFragment);
        });

        mSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                switch (contentTypeSelector.getCheckedRadioButtonId()){
                    case R.id.posts:
                        mPostsAdapter.getFilter().filter(newText);
                        break;
                    case R.id.pages:
                        mPagesAdapter.getFilter().filter(newText);
                        break;
                }
                return true;
            }
        });


        contentTypeSelector.check(R.id.posts);
        getPosts();

        contentTypeSelector.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton postsBtn = view.findViewById(R.id.posts);
            RadioButton pagesBtn = view.findViewById(R.id.pages);

            switch (checkedId){
                case R.id.posts:
                    postsBtn.setTextColor(getResources().getColor(R.color.white));
                    pagesBtn.setTextColor(getResources().getColor(R.color.black));
                    getPosts();
                    break;
                case R.id.pages:
                    pagesBtn.setTextColor(getResources().getColor(R.color.white));
                    postsBtn.setTextColor(getResources().getColor(R.color.black));
                    getPages();
                    break;
            }
        });
    }

    public void getPosts(){
        ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setCancelable(false);
        pDialog.setMessage("Processing please wait...");
        pDialog.show();

        String url = Urls.BASE_URL + Urls.GET_ALL_POSTS;

        posts = new ArrayList<>();

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
                                             Post post;
                                             for (int i = 0; i < array.length(); i++) {
                                                 JSONObject item = array.getJSONObject(i);
                                                 post = new Post();
                                                 post.setId(item.getInt("id"));
                                                 post.setTitle(item.getString("title"));
                                                 post.setContent(item.getString("description"));
                                                 post.setPublisherId(item.getInt("publisher_id"));
                                                 JSONObject publisher = item.getJSONObject("publisher");
                                                 post.setName(publisher.getString("first_name") + " " + publisher.getString("last_name"));
                                                 post.setImage(Urls.PUBLIC_FOLDER + publisher.getString("profile_photo_url"));
                                                 post.setReactions(item.getInt("reactions_count"));
                                                 post.setTools(item.getInt("tools_count"));
                                                 post.setReferences(item.getInt("references_count"));
                                                 post.setMyReaction(item.isNull("userReactiontype")? -1:item.getInt("userReactiontype"));

                                                 posts.add(post);
                                             }
                                             mPostsAdapter = new PostsAdapter(context, posts);
                                             mList.setAdapter(mPostsAdapter);
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

    public void getPages(){

        pages = new ArrayList<Page>()
        {{
            add(new Page(1, "Welcome\n" +
                    "\n" +
                    "We \"uniSupport\" offer you everything that is useful and what you need and all the important topics for you as students. We are all here to spread cooperation, arrange all distracting ideas and answer all questions. We are here to untie the knot and light the way for you.", "", "15/05/2022", 1, "UniSupport"));
        }};

        mPagesAdapter = new PagesAdapter(context, pages);
        mList.setAdapter(mPagesAdapter);

//        ProgressDialog pDialog = new ProgressDialog(context);
//        pDialog.setCancelable(false);
//        pDialog.setMessage("Processing please wait...");
//        pDialog.show();
//
//        String url = Urls.BASE_URL + Urls.GET_ALL_PAGES;
//
//        pages = new ArrayList<>();
//
//        AndroidNetworking.get(url)
//                .setPriority(Priority.MEDIUM)
//                .addQueryParameter("user_id", String.valueOf(SharedPrefManager.getInstance(context).getUserId()))
//                .build()
//                .getAsJSONObject(new JSONObjectRequestListener() {
//                                     @Override
//                                     public void onResponse(JSONObject response) {
//                                         pDialog.dismiss();
//                                         // do anything with response
//                                         try {
//                                             //converting response to json object
//                                             JSONObject obj = response;
//                                             String message = response.getString("message");
//
//                                             //if no error in response
//                                             Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
//                                             //getting the user from the response
//                                             JSONArray array = obj.getJSONArray("data");
//                                             Page page;
//                                             for (int i = 0; i < array.length(); i++) {
//                                                 JSONObject item = array.getJSONObject(i);
//                                                 page = new Page();
//                                                 page.setId(item.getInt("id"));
//                                                 page.setContent(item.getString("content_URL"));
//                                                 page.setUrl(item.getString("page_URL"));
//                                                 page.setDate(item.getString("date").split("T")[0]);
//                                                 JSONObject admin = item.getJSONObject("admin");
//                                                 page.setAdmin_id(admin.getInt("id"));
//                                                 page.setAdmin_name(admin.getString("first_name") + " " + admin.getString("last_name"));
//
//                                                 pages.add(page);
//                                             }
//                                             mPagesAdapter = new PagesAdapter(context, pages);
//                                             mList.setAdapter(mPagesAdapter);
//                                         } catch (JSONException e) {
//                                             e.printStackTrace();
//                                         }
//                                     }
//                                     @Override
//                                     public void onError(ANError error) {
//                                         pDialog.dismiss();
//                                         // handle error
//                                         Toast.makeText(context, error.getErrorBody(), Toast.LENGTH_SHORT).show();
//                                     }
//                                 }
//                );

    }



}