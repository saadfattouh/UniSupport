package com.example.unisupport.employee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.unisupport.R;
import com.example.unisupport.api.Urls;
import com.example.unisupport.utils.SharedPrefManager;
import com.example.unisupport.utils.Validation;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class AddPageActivity extends AppCompatActivity {

    TextInputEditText mContent, mLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_page);

        mContent = findViewById(R.id.content);
        mLink = findViewById(R.id.link);

        findViewById(R.id.publish_btn).setOnClickListener(v -> {
            if(Validation.validateInput(this, mContent, mLink)){
                createNewPage();
            }
        });
    }

    private void createNewPage() {

        final String content = mContent.getText().toString().trim();
        final String link = mLink.getText().toString().trim();

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();

        ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Processing please wait...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();

        String url = Urls.BASE_URL + Urls.ADD_NEW_PAGE;

        AndroidNetworking.post(url)
                .setPriority(Priority.MEDIUM)
                .addBodyParameter("content_URL", content)
                .addBodyParameter("page_URL", link)
                .addBodyParameter("date", formatter.format(date))
                .addBodyParameter("admin_id", String.valueOf(SharedPrefManager.getInstance(this).getUserId()))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        // do anything with response
                        JSONObject obj = response;
                        try {

                            Toast.makeText(AddPageActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            finish();

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
}