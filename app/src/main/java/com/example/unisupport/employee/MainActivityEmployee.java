package com.example.unisupport.employee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.unisupport.R;
import com.example.unisupport.employee.fragments.PagesFragment;
import com.example.unisupport.employee.fragments.RepliesAdministrativeFragment;
import com.example.unisupport.employee.fragments.StudentsConsultationsFragment;
import com.example.unisupport.pageradapters.FragmentsPager;
import com.kekstudio.dachshundtablayout.DachshundTabLayout;

public class MainActivityEmployee extends AppCompatActivity {


    //initializing variables
    DachshundTabLayout mCategoriesTabLayout;
    FragmentsPager mFragmentsAdapter;
    ViewPager mFragmentsViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_employee);


        //binding
        mCategoriesTabLayout = findViewById(R.id.categories_tab_layout);
        mFragmentsViewPager = findViewById(R.id.tags_pager);

        //set up pager with adapter
        mFragmentsAdapter = new FragmentsPager(getSupportFragmentManager());
        mFragmentsAdapter.addFragment(new StudentsConsultationsFragment(), "Requests");
        mFragmentsAdapter.addFragment(new RepliesAdministrativeFragment(), "Replies");
        //mFragmentsAdapter.addFragment(new PagesFragment(), "Lights");

        mFragmentsViewPager.setAdapter(mFragmentsAdapter);

        //setting up tabs layout with pager
        mCategoriesTabLayout.setupWithViewPager(mFragmentsViewPager);

    }


}