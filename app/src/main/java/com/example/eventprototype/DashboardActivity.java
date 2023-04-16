package com.example.eventprototype;

import androidx.annotation.ArrayRes;
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
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
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

    private BarChart dashFacBudget, dashEvents;
    private HorizontalBarChart dashEngagement;
    private PieChart dashUsersCat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        //get the intent passed from the LoginActivity
        currentUser = (ArrayList<UserModel>) getIntent().getSerializableExtra("currentUser");
        System.out.println(currentUser.get(0).getUsername());

        dashUsersCat = findViewById(R.id.dashUsersCat);
        dashScroll = findViewById(R.id.dashScroll);
        transparent = findViewById(R.id.imageTrans);
        dashUser = findViewById(R.id.dashUser);
        dashFacBudget = findViewById(R.id.dashFacBudget);
        dashEngagement = findViewById(R.id.dashEngagement);
        dashEvents = findViewById(R.id.dashEvents);

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

        //events bar chart
        List<EventModel> eventData = db.eventFaculty();
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        for (int i = 0; i < eventData.size(); i++) {
            System.out.println(eventData.get(i).getIsJoin());
            System.out.println(eventData.get(i).getFaculty());
            entries.add(new BarEntry(i, eventData.get(i).getIsJoin()));
            labels.add(eventData.get(i).getFaculty());
        }

        //calling method for faculty events
        eventsBarChart(entries, labels);



        //engagement bar chart
        List<EventModel> eData = db.engagementCountry();
        ArrayList<BarEntry> xValues = new ArrayList<>();
        ArrayList<String> yValues = new ArrayList<>();
        for (int i = 0; i < eData.size(); i++) {
            xValues.add(new BarEntry(i, eData.get(i).getIsJoin()));
            yValues.add(eData.get(i).getLocation());
        }


        //calling method to set horizontal chart
        hBarChart(xValues, yValues);

        //domestic vs internation pie chart
        List<UserModel> domesticUsers = db.domesticUsers();
        List<UserModel> internationalUsers = db.internationalUsers();
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(domesticUsers.get(0).getIsInternational(), "Domestic"));
        pieEntries.add(new PieEntry(internationalUsers.get(0).getIsInternational(), "International"));
        //calling method to set pie chart
        setPieChart(pieEntries);
    }

    public void hBarChart(ArrayList<BarEntry> xValues, ArrayList<String> yValues) {
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
        hBarDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        dashEngagement.getAxisRight().setEnabled(false);
        yAxisH.setTextColor(Color.WHITE);
    }

    public void eventsBarChart(ArrayList<BarEntry> x, ArrayList<String> y) {
        BarDataSet barDataSet = new BarDataSet(x, null);
        barDataSet.setValueTextColor(Color.WHITE);
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        Description description = new Description();
        description.setText("Event Count per Faculty");
        description.setTextColor(Color.WHITE);
        dashEvents.setDescription(description);
        BarData barData = new BarData(barDataSet);
        dashEvents.setData(barData);
        //set X Axis formatter
        XAxis xAxis = dashEvents.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(y));
        //set position of labels
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setGranularity(1f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setLabelCount(y.size());
        //set Y Axis formatter
        YAxis yAxis = dashEvents.getAxisLeft();
        barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        dashEvents.getAxisRight().setEnabled(false);
        yAxis.setTextColor(Color.WHITE);
    }


    public void setPieChart(ArrayList<PieEntry> pieEntries) {
            PieDataSet pieDataSet = new PieDataSet(pieEntries, null);
        Description desc = new Description();
        desc.setText("Users by category");
        desc.setTextColor(Color.WHITE);
        PieData pieData = new PieData(pieDataSet);
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(12f);
        Legend legend = dashUsersCat.getLegend();
        legend.setTextColor(Color.WHITE);
        dashUsersCat.setDescription(desc);
        dashUsersCat.setData(pieData);
    }



}