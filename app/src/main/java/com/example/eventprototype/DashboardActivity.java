package com.example.eventprototype;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.example.eventprototype.Model.UserModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private ArrayList<UserModel> currentUser;

    private ScrollView dashScroll;
    private ImageView transImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        //get the intent passed from the LoginActivity
        currentUser = (ArrayList<UserModel>) getIntent().getSerializableExtra("currentUser");
        System.out.println(currentUser.get(0).getUsername());
        //ScrollView dashScroll = findViewById(R.id.dashScroll);
        //transImage = findViewById(R.id.imageTrans);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        //set the icon as selected
        bottomNavigationView.setSelectedItemId(R.id.dashboard);
        Fragment fragment = new MapFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.eventMapFrame, fragment).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.events:
                        Intent intent = new Intent(DashboardActivity.this, MainActivity.class );
                        intent.putExtra("currentUser", currentUser);
                        startActivity(intent);
                        return true;

                    case R.id.profile:
                        intent = new Intent(DashboardActivity.this, ProfileActivity.class );
                        intent.putExtra("currentUser", currentUser);
                        startActivity(intent);
                        return true;
                }
                return false;
            }
        });



    }
}