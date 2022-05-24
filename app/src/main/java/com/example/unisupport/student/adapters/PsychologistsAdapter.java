package com.example.unisupport.student.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.unisupport.R;
import com.example.unisupport.api.Urls;
import com.example.unisupport.model.Psychologist;
import com.example.unisupport.utils.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PsychologistsAdapter extends RecyclerView.Adapter<PsychologistsAdapter.ViewHolder> {


    Context context;
    private List<Psychologist> items;

    // RecyclerView recyclerView;
    public PsychologistsAdapter(Context context, ArrayList<Psychologist> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_psychologist, parent, false);

        return new ViewHolder(listItem);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Psychologist item = items.get(position);

        holder.name.setText(item.getName());

        holder.itemView.setOnClickListener(v -> {
            LayoutInflater factory = LayoutInflater.from(context);
            final View infoDialogView = factory.inflate(R.layout.ask_question_dialog, null);
            final AlertDialog askQuestionDialog = new AlertDialog.Builder(context).create();
            askQuestionDialog.setView(infoDialogView);
            askQuestionDialog.setCanceledOnTouchOutside(true);

            //to look rounded
            askQuestionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            EditText mContent = infoDialogView.findViewById(R.id.details);
            Button send = infoDialogView.findViewById(R.id.send);

            send.setOnClickListener(v1 -> {
                if(mContent.getText().toString().isEmpty()){
                    Toast.makeText(context, "you can't send an empty question!", Toast.LENGTH_SHORT).show();
                }else {
                    sendQuestion(item, mContent.getText().toString().trim());
                }
            });

            askQuestionDialog.show();
        });


    }

    private void sendQuestion(Psychologist psychologist, String content) {
        ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        String url = Urls.BASE_URL + Urls.ASK_PSYCHOLOGIST;

        AndroidNetworking.post(url)
                .addBodyParameter("student_id", String.valueOf(SharedPrefManager.getInstance(context).getUserId()))
                .addBodyParameter("psychologist_id", String.valueOf(psychologist.getId()))
                .addBodyParameter("description", content)
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


    @Override
    public int getItemCount() {
        return items.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public CircleImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.name);
            this.image  = itemView.findViewById(R.id.image);
        }

    }


}