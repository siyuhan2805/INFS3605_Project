package com.example.eventprototype;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.navigation.NavigationBarView;

public class DashboardActivity extends AppCompatActivity {
    private NavigationBarView navigationView;
    private TextView menuTitle;
    private ImageView logoutBtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //set up the toolbar
        menuTitle = findViewById(R.id.toolbar_heading_logout);
        menuTitle.setText("Dashboard");
        logoutBtn = findViewById(R.id.logoutIcon);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this,
                        LoginActivity.class);
                startActivity(intent);
            }
        });

        //Set up nav bar
        navigationView = findViewById(R.id.navView);

        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent = new Intent();
                switch (item.getItemId()) {
                    case R.id.nav_profile:
                        intent = new Intent(DashboardActivity.this,
                                ProfileActivity.class);
                        //TODO: intent of user needs to be passed to each scene
                        //intent.putExtra("currentUser", userList);
                        startActivity(intent);
                        break;
                    case R.id.nav_add_event:
                        intent = new Intent(DashboardActivity.this,
                                AddNewEvent.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_home:
                        intent = new Intent(DashboardActivity.this,
                                DashboardActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_feed:
                        //TODO: make social media feed
                        break;
                    case R.id.nav_data:
                        //TODO: make nav data page
                        break;
                }
                return true;
            }
        });
    }


}