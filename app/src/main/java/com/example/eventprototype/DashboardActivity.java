package com.example.eventprototype;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventprototype.Model.UserModel;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jwt.*;
//import com.nimbusds.jwt.JWTClaimsSet.Builder;

//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

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
        dashboardView.getSettings().setJavaScriptEnabled(true);

        /*
        String dashboardUrl = "https://prod-apsoutheast-a.online.tableau.com/t/infs3605/views/INFS3605/Dashboard1";
        String htmlData = "<script type='module' src='https://prod-apsoutheast-a.online.tableau.com/javascripts/api/tableau.embedding.3.latest.min.js'></script><tableau-viz id='tableau-viz' src='https://prod-apsoutheast-a.online.tableau.com/t/infs3605/views/INFS3605/Dashboard1' width='1247' height='570' hide-tabs toolbar='bottom' device='default' ></tableau-viz>";
        dashboardView.loadDataWithBaseURL(dashboardUrl, htmlData, "text/html", "UTF-8", null);
         */

        dashboardView.loadUrl("https://unswerm.github.io/");

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
