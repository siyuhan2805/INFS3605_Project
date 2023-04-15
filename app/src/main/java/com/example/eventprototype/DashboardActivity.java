package com.example.eventprototype;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.eventprototype.Db.DatabaseHandler;
import com.example.eventprototype.Model.EventModel;
import com.example.eventprototype.Model.UserModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.transition.MaterialSharedAxis;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private ArrayList<UserModel> currentUser;
    private DatabaseHandler db;
    private NestedScrollView dashScroll;
    private ImageView transparent;
    private TextView dashUser;

    private BarChart dashFacBudget;
    private HorizontalBarChart dashEngagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        //get the intent passed from the LoginActivity
        currentUser = (ArrayList<UserModel>) getIntent().getSerializableExtra("currentUser");
        System.out.println(currentUser.get(0).getUsername());

        dashScroll = findViewById(R.id.dashScroll);
        transparent = findViewById(R.id.imageTrans);
        dashUser = findViewById(R.id.dashUser);
        dashFacBudget = findViewById(R.id.dashFacBudget);
        dashEngagement = findViewById(R.id.dashEngagement);

        //initialise db
        db = new DatabaseHandler(this);
        //open database
        db.openDatabase();

        //sets the name of the user that is logged in on the front screen
        dashUser.setText("Hello " + currentUser.get(0).getFirstName());

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

        //budget bar chart
        List<EventModel> data = db.budgetFaculty();
        ArrayList<BarEntry> barEntryArrayList = new ArrayList<>();
        ArrayList<String> labelNames = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            barEntryArrayList.add(new BarEntry(i, data.get(i).getBudget()));
            labelNames.add(data.get(i).getFaculty());
        }

        BarDataSet barDataSet = new BarDataSet(barEntryArrayList, null);
        barDataSet.setValueTextColor(Color.WHITE);
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        Description description = new Description();
        description.setText("Total Budget per Faculty");
        description.setTextColor(Color.WHITE);
        dashFacBudget.setDescription(description);
        BarData barData = new BarData(barDataSet);
        dashFacBudget.setData(barData);
        //set X Axis formatter
        XAxis xAxis = dashFacBudget.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labelNames));
        //set position of labels
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setGranularity(1f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setLabelCount(labelNames.size());
        //set Y Axis formatter
        YAxis yAxis = dashFacBudget.getAxisLeft();
        barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        dashFacBudget.getAxisRight().setEnabled(false);
        yAxis.setTextColor(Color.WHITE);

        //engagement bar chart
        List<EventModel> eData = db.engagementCountry();
        ArrayList<BarEntry> xValues = new ArrayList<>();
        ArrayList<String> yValues = new ArrayList<>();
        for (int i = 0; i < eData.size(); i++) {
            xValues.add(new BarEntry(i, eData.get(i).getIsJoin()));
            yValues.add(eData.get(i).getLocation());
        }

        BarDataSet hBarDataSet = new BarDataSet(xValues, null);
        hBarDataSet.setValueTextColor(Color.WHITE);
        hBarDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        Description desc = new Description();
        desc.setText("Engagement by Country");
        dashEngagement.setDescription(desc);
        desc.setTextColor(Color.WHITE);
        BarData hBarData = new BarData(hBarDataSet);
        dashEngagement.setData(hBarData);
        //set X Axis formatter
        XAxis xAxisH = dashEngagement.getXAxis();
        xAxisH.setValueFormatter(new IndexAxisValueFormatter(yValues));
        //set position of labels
        xAxisH.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxisH.setDrawGridLines(false);
        xAxisH.setDrawAxisLine(true);
        xAxisH.setGranularity(1f);
        xAxisH.setTextColor(Color.WHITE);
        xAxisH.setLabelCount(yValues.size());
        //set Y Axis formatter
        YAxis yAxisH = dashEngagement.getAxisLeft();
        barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        dashEngagement.getAxisRight().setEnabled(false);
        yAxisH.setTextColor(Color.WHITE);








    }
}