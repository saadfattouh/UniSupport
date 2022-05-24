package com.example.unisupport.student.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.developer.filepicker.controller.DialogSelectionListener;
import com.developer.filepicker.model.DialogConfigs;
import com.developer.filepicker.model.DialogProperties;
import com.developer.filepicker.view.FilePickerDialog;
import com.example.unisupport.Constants;
import com.example.unisupport.R;
import com.example.unisupport.api.Urls;
import com.example.unisupport.model.Post;
import com.example.unisupport.model.Reference;
import com.example.unisupport.model.Tool;
import com.example.unisupport.student.PsychologistReplyViewActivity;
import com.example.unisupport.utils.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> implements Filterable {


    Context context;
    private List<Post> posts;
    private List<Post> itemsFiltered;

    SharedPrefManager prefManager;
    int myId;

    // RecyclerView recyclerView;
    public PostsAdapter(Context context, ArrayList<Post> posts) {
        this.context = context;
        this.posts = posts;
        this.itemsFiltered = posts;
        prefManager = SharedPrefManager.getInstance(context);
        myId = prefManager.getUserId();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_post, parent, false);

        return new ViewHolder(listItem);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Post post = posts.get(position);

        holder.name.setText(post.getName());
        holder.date.setText(post.getDate());

        Glide.with(context).load(post.getImage()).into(holder.image);

        holder.title_content.setText(post.getTitle() + "\n" + post.getContent());
        holder.reactions.setText("" + post.getReactions() + " reactions");
        holder.tools.setText("" + post.getTools() + " tools");
        holder.references.setText("" + post.getReferences() + " references");


            switch (post.getMyReaction()){
                case Constants.UN_USE_FULL:
                    holder.reactionsGroup.check(R.id.un_useful);
                    break;
                case Constants.USEFUL:
                    holder.reactionsGroup.check(R.id.useful);
                    break;
                case Constants.GOOD:
                    holder.reactionsGroup.check(R.id.good);
                    break;
                case Constants.NECESSARY:
                    holder.reactionsGroup.check(R.id.necessary);
                    break;
                case Constants.BRILLIANT:
                    holder.reactionsGroup.check(R.id.brilliant);
                    break;
                default:holder.reactionsGroup.clearCheck();
            }


        holder.reactionsGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId){
                case R.id.un_useful:
                    updateMyReaction(myId, post.getId(), Constants.UN_USE_FULL);
                    break;
                case R.id.useful:
                    updateMyReaction(myId, post.getId(), Constants.USEFUL);
                    break;
                case R.id.good:
                    updateMyReaction(myId, post.getId(), Constants.GOOD);
                    break;
                case R.id.necessary:
                    updateMyReaction(myId, post.getId(), Constants.NECESSARY);
                    break;
                case R.id.brilliant:
                    updateMyReaction(myId, post.getId(), Constants.BRILLIANT);
                    break;
            }
        });

        holder.tools.setOnClickListener(v -> {
            showToolsDialog(post);
        });

        holder.references.setOnClickListener(v -> {
            showReferencesDialog(post);
        });

        holder.reactions.setOnClickListener(v -> {
            showReactionsDialog(post);
        });

    }

    private void showReactionsDialog(Post post) {
        LayoutInflater factory = LayoutInflater.from(context);
        final View view = factory.inflate(R.layout.dialog_reactions, null);
        final AlertDialog questionAnswerDialog = new AlertDialog.Builder(context).create();
        questionAnswerDialog.setView(view);

        //to look rounded
        questionAnswerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView unUseful = view.findViewById(R.id.un_useful);
        TextView useful = view.findViewById(R.id.useful);
        TextView good = view.findViewById(R.id.good);
        TextView necessary = view.findViewById(R.id.necessary);
        TextView brilliant = view.findViewById(R.id.brilliant);

        String url = Urls.BASE_URL + Urls.GET_POST_REACTIONS;

        AndroidNetworking.get(url)
                .setPriority(Priority.MEDIUM)
                .addQueryParameter("topic_id", String.valueOf(post.getId()))
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
                                             int unusefulCount = 0, usefulCount = 0, necessaryCount = 0, goodCount = 0, brilliantCount = 0;
                                             for (int i = 0; i < array.length(); i++) {
                                                 JSONObject item = array.getJSONObject(i);
                                                 switch (item.getInt("reaction_type")){
                                                     case Constants.UN_USE_FULL:
                                                        unusefulCount++;
                                                        break;
                                                     case Constants.USEFUL:
                                                         usefulCount++;
                                                         break;
                                                     case Constants.GOOD:
                                                         goodCount++;
                                                         break;
                                                     case Constants.NECESSARY:
                                                         necessaryCount++;
                                                         break;
                                                     case Constants.BRILLIANT:
                                                         brilliantCount++;
                                                         break;
                                                 }
                                             }

                                             unUseful.setText(String.valueOf("UnUseful\n" + unusefulCount));
                                             useful.setText(String.valueOf("Useful\n" + usefulCount));
                                             good.setText(String.valueOf("Good\n" + goodCount));
                                             necessary.setText(String.valueOf("Necessary\n" + necessaryCount));
                                             brilliant.setText(String.valueOf("Brilliant\n" + brilliantCount));

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

        questionAnswerDialog.show();
    }

    private void showReferencesDialog(Post post) {
        LayoutInflater factory = LayoutInflater.from(context);
        final View view = factory.inflate(R.layout.dialog_references, null);
        final AlertDialog referencesDialog = new AlertDialog.Builder(context).create();
        referencesDialog.setView(view);

        //to look rounded
        referencesDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView addReferenceBtn = view.findViewById(R.id.upload_btn);

        if(post.getPublisherId() == myId){
            addReferenceBtn.setOnClickListener(v -> {
                referencesDialog.dismiss();
                uploadNewReference(post);
            });
        }else {
            addReferenceBtn.setVisibility(View.GONE);
        }



        getAllReferences(post.getId(), view);

        referencesDialog.show();
    }

    private void uploadNewReference(Post post) {
        DialogProperties properties = new DialogProperties();
        properties.selection_mode = DialogConfigs.SINGLE_MODE;
        properties.selection_type = DialogConfigs.FILE_SELECT;
        properties.root = new File(DialogConfigs.DEFAULT_DIR);
        properties.error_dir = new File(DialogConfigs.DEFAULT_DIR);
        properties.offset = new File(DialogConfigs.DEFAULT_DIR);
        properties.extensions = new String[]{".pdf"};
        properties.show_hidden_files = false;
        FilePickerDialog dialog = new FilePickerDialog(context, properties);
        dialog.setTitle("Select a File");
        dialog.setDialogSelectionListener(new DialogSelectionListener() {
            @Override
            public void onSelectedFilePaths(String[] files) {
                //files is the array of the paths of files selected by the Application User.
                if(files[0] != null){
                    uploadReference(post, files[0]);
                }
            }
        });
        dialog.show();
    }

    private void uploadReference(Post post, String file) {

        ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        String url = Urls.BASE_URL + Urls.ADD_REFERENCE;

        AndroidNetworking.upload(url)
                .addMultipartParameter("post_id", String.valueOf(post.getId()))
                .addMultipartFile("file_Url", new File(file))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();

                        try {
                            String message = response.getString("message");

                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
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

    private void getAllReferences(int id, View view) {
        RecyclerView mList = view.findViewById(R.id.rv);
        ArrayList<Reference> references = new ArrayList<>();

        String url = Urls.BASE_URL + Urls.GET_POST_REFERENCES;

        AndroidNetworking.get(url)
                .setPriority(Priority.MEDIUM)
                .addQueryParameter("topic_id", String.valueOf(id))
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
                                             Reference reference;
                                             for (int i = 0; i < array.length(); i++) {
                                                 JSONObject item = array.getJSONObject(i);
                                                 reference = new Reference();
                                                 reference.setId(item.getInt("id"));
                                                 reference.setTopic_id(item.getInt("topic_id"));
                                                 reference.setUrl(Urls.PUBLIC_FOLDER + item.getString("file_Url"));

                                                 references.add(reference);
                                             }
                                             ReferencesAdapter mAdapter = new ReferencesAdapter(context, references);
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

    private void showToolsDialog(Post post) {
        LayoutInflater factory = LayoutInflater.from(context);
        final View view = factory.inflate(R.layout.dialog_tool, null);
        final AlertDialog toolsDialog = new AlertDialog.Builder(context).create();
        toolsDialog.setView(view);
        toolsDialog.setCanceledOnTouchOutside(true);

        //to look rounded
        toolsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        EditText mLinkEt = view.findViewById(R.id.tool_link);
        Button addToolBtn = view.findViewById(R.id.add_tool_btn);

        if(post.getPublisherId() == myId){
            addToolBtn.setOnClickListener(v -> {
                if(!mLinkEt.getText().toString().isEmpty()){
                    uploadNewTool(post.getId(), mLinkEt.getText().toString());
                    toolsDialog.dismiss();
                }
            });
        }else {
            addToolBtn.setVisibility(View.GONE);
            mLinkEt.setVisibility(View.GONE);
        }

        getAllTools(post.getId(), view);



        toolsDialog.show();
    }

    private void uploadNewTool(int id, String toolLink) {
        ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        String url = Urls.BASE_URL + Urls.ADD_TOOL;

        AndroidNetworking.post(url)
                .addBodyParameter("post_id", String.valueOf(id))
                .addBodyParameter("name", toolLink)
                .addBodyParameter("link", toolLink)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();

                        try {
                            String message = response.getString("message");

                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
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

    private void getAllTools(int id, View view) {
        RecyclerView mList = view.findViewById(R.id.rv);
        ArrayList<Tool> tools = new ArrayList<>();

        String url = Urls.BASE_URL + Urls.GET_POST_TOOLS;

        AndroidNetworking.get(url)
                .setPriority(Priority.MEDIUM)
                .addQueryParameter("topic_id", String.valueOf(id))
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
                                             Tool tool;
                                             for (int i = 0; i < array.length(); i++) {
                                                 JSONObject item = array.getJSONObject(i);
                                                 tool = new Tool();
                                                 tool.setId(item.getInt("id"));
                                                 tool.setName(item.getString("name"));
                                                 tool.setTopic_id(item.getInt("topic_id"));
                                                 tool.setUrl(item.getString("link"));

                                                 tools.add(tool);
                                             }
                                             ToolsAdapter mAdapter = new ToolsAdapter(context, tools);
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


    private void updateMyReaction(int userId, int postId, int reaction){
        String url = Urls.BASE_URL + Urls.UPDATE_USER_REACTION;

        AndroidNetworking.post(url)
                .setPriority(Priority.MEDIUM)
                .addBodyParameter("user_id", String.valueOf(userId))
                .addBodyParameter("post_id", String.valueOf(postId))
                .addBodyParameter("reaction", String.valueOf(reaction))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        JSONObject obj = response;
                        try {

                            Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.e("Error", error.getErrorBody());

                    }
                });
    }



    @Override
    public int getItemCount() {
        return posts.size();
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();

                if(charSequence == null | charSequence.length() == 0){
                    filterResults.count = itemsFiltered.size();
                    filterResults.values = itemsFiltered;

                }else{
                    String searchChr = charSequence.toString().toLowerCase();
                    ArrayList<Post> resultData = new ArrayList<>();


                    for(Post post: itemsFiltered){
                        if(post.getContent().toLowerCase().contains(searchChr)){
                            resultData.add(post);
                        }
                    }


                    filterResults.count = resultData.size();
                    filterResults.values = resultData;

                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                posts = (ArrayList<Post>) filterResults.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name, date, title_content, tools, references, reactions;
        public CircleImageView image;
        public RadioGroup reactionsGroup;

        public ViewHolder(View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.name);
            this.date = itemView.findViewById(R.id.date);
            this.title_content = itemView.findViewById(R.id.title_content);
            this.tools = itemView.findViewById(R.id.tools);
            this.references = itemView.findViewById(R.id.references);
            this.reactions = itemView.findViewById(R.id.reactions);
            this.image = itemView.findViewById(R.id.image);
            this.reactionsGroup = itemView.findViewById(R.id.reactions_group);

        }
    }


}