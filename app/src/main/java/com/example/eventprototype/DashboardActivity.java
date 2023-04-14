package com.example.eventprototype;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.example.eventprototype.Model.UserModel;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private ArrayList<UserModel> currentUser;

    private NestedScrollView dashScroll;
    private ImageView transparent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        //get the intent passed from the LoginActivity
        currentUser = (ArrayList<UserModel>) getIntent().getSerializableExtra("currentUser");
        System.out.println(currentUser.get(0).getUsername());

        dashScroll = findViewById(R.id.dashScroll);
        transparent = findViewById(R.id.imageTrans);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        //set the icon as selected
        bottomNavigationView.setSelectedItemId(R.id.dashboard);
        Fragment fragment = new MapFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.eventMapFrame, fragment).commit();

        //workaround to make sure you can scroll on the Maps

        transparent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        dashScroll.requestDisallowInterceptTouchEvent(true);
                        // Disable touch on transparent view
                        return false;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        dashScroll.requestDisallowInterceptTouchEvent(false);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        dashScroll.requestDisallowInterceptTouchEvent(true);
                        return false;

                    default:
                        return true;
                }
            }
        });

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