package com.example.eventprototype;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventprototype.Model.UserModel;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {
    private NavigationBarView navigationView;
    private TextView menuTitle;
    private ImageView logoutBtn;
    private ArrayList<UserModel> userList;
    private WebView dashboardView;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //set up dashboard
        dashboardView = findViewById(R.id.dashboardView);
        String dashboardUrl = "https://app.powerbi.com/";
        String iframeSrc = "https://app.powerbi.com/view?r=eyJrIjoiODFlMWJlNmYtNjFjYi00ZmY5LTk2ZmEtNWM3MmQwNTFlNWU3IiwidCI6IjliN2JmYmI2LTllN2EtNDFlYS05NDIwLThhY2VkNDM3ZDM0YiJ9";
        String htmlData = "<iframe title=\"Report Section\" width=\"380\" height=\"500\" src=\'" + iframeSrc + "'' frameborder=\"0\" allowFullScreen=\"true\"></iframe>";
        dashboardView.loadDataWithBaseURL(dashboardUrl, htmlData, "text/html", "UTF-8", null);

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
                        intent.putExtra("currentUser", userList);
                        startActivity(intent);
                        break;
                    case R.id.nav_add_event:
                        intent = new Intent(DashboardActivity.this,
                                AddNewEvent.class);
                        intent.putExtra("currentUser", userList);
                        startActivity(intent);
                        break;
                    case R.id.nav_home:
                        intent = new Intent(DashboardActivity.this,
                                DashboardActivity.class);
                        intent.putExtra("currentUser", userList);
                        startActivity(intent);
                        break;
                    case R.id.nav_feed:
                        intent = new Intent(DashboardActivity.this,
                                MainActivity.class);
                        intent.putExtra("currentUser", userList);
                        startActivity(intent);
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
