package com.example.unisupport.psychologist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.unisupport.R;
import com.example.unisupport.pageradapters.FragmentsPager;
import com.example.unisupport.psychologist.fragments.RepliesPsychologistFragment;
import com.example.unisupport.psychologist.fragments.StudentsQuestionsFragment;
import com.example.unisupport.student.fragments.ChatsFragment;
import com.example.unisupport.utils.SharedPrefManager;
import com.kekstudio.dachshundtablayout.DachshundTabLayout;

public class MainActivityPsychologist extends AppCompatActivity {

    //initializing variables
    DachshundTabLayout mCategoriesTabLayout;
    FragmentsPager mFragmentsAdapter;
    ViewPager mFragmentsViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_psychologist);

        //binding
        mCategoriesTabLayout = findViewById(R.id.categories_tab_layout);
        mFragmentsViewPager = findViewById(R.id.tags_pager);

        //set up pager with adapter
        mFragmentsAdapter = new FragmentsPager(getSupportFragmentManager());
        mFragmentsAdapter.addFragment(new StudentsQuestionsFragment(), "Requests");
        mFragmentsAdapter.addFragment(new RepliesPsychologistFragment(), "Replies");
        mFragmentsAdapter.addFragment(new ChatsFragment(), "Chats");
        mFragmentsViewPager.setAdapter(mFragmentsAdapter);

        //setting up tabs layout with pager
        mCategoriesTabLayout.setupWithViewPager(mFragmentsViewPager);

        ImageView mLogoutBtn = findViewById(R.id.logout);

        mLogoutBtn.setOnClickListener(v -> {
            SharedPrefManager.getInstance(this).logout();
            PackageManager packageManager = getPackageManager();
            Intent intent = packageManager.getLaunchIntentForPackage(getPackageName());
            ComponentName componentName = intent.getComponent();
            Intent mainIntent = Intent.makeRestartActivityTask(componentName);
            startActivity(mainIntent);
            Runtime.getRuntime().exit(0);
        });

    }
}