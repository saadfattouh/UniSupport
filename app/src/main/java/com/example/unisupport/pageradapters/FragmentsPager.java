package com.example.unisupport.pageradapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentsPager extends FragmentPagerAdapter {

    //initialize array of fragments and array of titles
    ArrayList<String> arrayList = new ArrayList<>();
    List<Fragment> fragmentList = new ArrayList<>();


    public void addFragment(Fragment fragment, String title){
        //add title
        arrayList.add(title);
        //add fragment
        fragmentList.add(fragment);
    }

    public FragmentsPager(@NonNull FragmentManager fm) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        //return fragment position
        return fragmentList.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return arrayList.get(position);
    }

    @Override
    public int getCount() {
        //return fragment list size
        return arrayList.size();
    }
}
