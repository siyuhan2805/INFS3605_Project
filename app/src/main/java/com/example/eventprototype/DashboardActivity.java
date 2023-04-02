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
        //String dashboardUrl = "https://public.tableau.com/shared/MHTWW4D6W?:display_count=n&:origin=viz_share_link&:embed=y";
        //String htmlData = "<div class='tableauPlaceholder' id='viz1680405248722' style='position: relative'><noscript><a href='https:&#47;&#47;www.mckinsey.com&#47;global-themes&#47;artificial-intelligence&#47;notes-from-the-ai-frontier-applications-and-value-of-deep-learning'><img alt='Estimated impact of artificial intelligence and other analytics by industry, function and business problem. ' src='https:&#47;&#47;public.tableau.com&#47;static&#47;images&#47;ST&#47;STDCFJM79&#47;1_rss.png' style='border: none' /></a></noscript><object class='tableauViz'  style='display:none;'><param name='host_url' value='https%3A%2F%2Fpublic.tableau.com%2F' /> <param name='embed_code_version' value='3' /> <param name='path' value='shared&#47;STDCFJM79' /> <param name='toolbar' value='yes' /><param name='static_image' value='https:&#47;&#47;public.tableau.com&#47;static&#47;images&#47;ST&#47;STDCFJM79&#47;1.png' /> <param name='animate_transition' value='yes' /><param name='display_static_image' value='yes' /><param name='display_spinner' value='yes' /><param name='display_overlay' value='yes' /><param name='display_count' value='yes' /><param name='filter' value='showVizhome=no' /></object></div>                <script type='text/javascript'>                    var divElement = document.getElementById('viz1680405248722');                    var vizElement = divElement.getElementsByTagName('object')[0];                    vizElement.style.width='640px';vizElement.style.height='1227px';                    var scriptElement = document.createElement('script');                    scriptElement.src = 'https://public.tableau.com/javascripts/api/viz_v1.js';                    vizElement.parentNode.insertBefore(scriptElement, vizElement);                </script>";
        //dashboardView.loadDataWithBaseURL(dashboardUrl, htmlData, "text/html", "UTF-8", null);

        dashboardView.getSettings().setJavaScriptEnabled(true);
        dashboardView.loadUrl("https://public.tableau.com/shared/MHTWW4D6W?:showVizHome=no");

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
