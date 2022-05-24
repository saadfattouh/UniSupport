package com.example.unisupport.student.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.unisupport.R;
import com.example.unisupport.model.Psychologist;
import com.example.unisupport.pageradapters.FragmentsPager;
import com.example.unisupport.student.adapters.PsychologistsAdapter;
import com.example.unisupport.student.fragments.administrative_childs.AdministrativeConsultationFragment;
import com.example.unisupport.student.fragments.administrative_childs.AdministrativeRepliesFragment;
import com.example.unisupport.student.fragments.psychologists_childs.AskPsychologistFragment;
import com.example.unisupport.student.fragments.psychologists_childs.PsychologistsRepliesFragment;
import com.kekstudio.dachshundtablayout.DachshundTabLayout;

import java.util.ArrayList;


public class PsychologistsFragment extends Fragment {

    //initializing variables
    DachshundTabLayout mCategoriesTabLayout;
    FragmentsPager mFragmentsAdapter;
    ViewPager mFragmentsViewPager;

    Context context;

    public PsychologistsFragment() {
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
        return inflater.inflate(R.layout.fragment_psychologists, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //binding
        mCategoriesTabLayout = view.findViewById(R.id.psychologists_tabs);
        mFragmentsViewPager = view.findViewById(R.id.pager);

        //set up pager with adapter
        mFragmentsAdapter = new FragmentsPager(getChildFragmentManager());
        mFragmentsAdapter.addFragment(new AskPsychologistFragment(), "Requests");
        mFragmentsAdapter.addFragment(new PsychologistsRepliesFragment(), "Replies");
        mFragmentsViewPager.setAdapter(mFragmentsAdapter);

        //setting up tabs layout with pager
        mCategoriesTabLayout.setupWithViewPager(mFragmentsViewPager);


    }
}