package com.example.unisupport.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.unisupport.R;
import com.example.unisupport.utils.SharedPrefManager;
import com.google.android.material.navigation.NavigationView;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivityStudent extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DrawerLayout.DrawerListener {

    public Toolbar toolbar;

    public DrawerLayout drawerLayout;

    public CircleImageView mProfileBtn;

    public NavController navController;

    public NavigationView navigationView;

    int destination = R.id.student_community;

    AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_student);

        setupNavigation();

    }

    private void setupNavigation() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerLayout = findViewById(R.id.my_drawer_layout);


        navigationView = findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);

        mProfileBtn = headerView.findViewById(R.id.profile_btn);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.student_community, R.id.student_messages, R.id.student_psychologists, R.id.student_administrative, R.id.student_profile).setOpenableLayout(drawerLayout).build();

        mProfileBtn.setOnClickListener(v -> {
            destination = R.id.student_profile;
            drawerLayout.closeDrawers();
        });

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);

        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(this);

        drawerLayout.addDrawerListener(this);

    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        menuItem.setChecked(true);

        int id = menuItem.getItemId();

        switch (id) {

            case R.id.student_community:
                destination = R.id.student_community;
                break;

            case R.id.student_messages:
                destination = R.id.student_messages;
                break;

            case R.id.student_psychologists:
                destination = R.id.student_psychologists;
                break;

            case R.id.logout:
                logout();

            case R.id.student_administrative:
                if(SharedPrefManager.getInstance(this).getStudentEmail().matches("^s[0-9]+@std\\.su\\.edu\\.sa$")){
                    destination = R.id.student_administrative;
                }else {
                    Toast.makeText(this, "this service is only available for Shaqraa university students", Toast.LENGTH_SHORT).show();
                }
                break;

        }

        drawerLayout.closeDrawers();

        return true;
    }

    private void logout() {
        SharedPrefManager.getInstance(this).logout();
        PackageManager packageManager = getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(getPackageName());
        ComponentName componentName = intent.getComponent();
        Intent mainIntent = Intent.makeRestartActivityTask(componentName);
        startActivity(mainIntent);
        Runtime.getRuntime().exit(0);
    }


    @Override
    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(@NonNull View drawerView) {

    }

    @Override
    public void onDrawerClosed(@NonNull View drawerView) {
        navController.navigate(destination);
    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }
}