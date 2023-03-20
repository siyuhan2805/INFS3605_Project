package com.example.eventprototype;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.eventprototype.Adapter.EventAdapter;
import com.example.eventprototype.Adapter.UpcomingEventsAdapter;
import com.example.eventprototype.Db.DatabaseHandler;
import com.example.eventprototype.Model.EventModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements DialogCloseListener{

    private RecyclerView eventsRecyclerView, upcomingEventsRecyclerView;
    private EventAdapter adapter;
    private UpcomingEventsAdapter upcomingEventsAdapter;
    private List<EventModel> eventList, upEventList;
    private DatabaseHandler db;
    private FloatingActionButton fab;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        //initialise fab
        fab = findViewById(R.id.fab);
        //initialise db
        db = new DatabaseHandler(this);
        //open database
        db.openDatabase();


        //initialise ItemTouchHelper
        //ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(adapter));
        //itemTouchHelper.attachToRecyclerView(eventsRecyclerView);

        //initialise eventList
        eventList = new ArrayList<>();

        //initialise upcoming events recyclerview
        upcomingEventsRecyclerView = findViewById(R.id.upcomingEventsRecyclerView);
        upcomingEventsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        //setting upcomingEventsAdapter to the RecyclerView;
        upcomingEventsAdapter = new UpcomingEventsAdapter(db, this);
        upcomingEventsRecyclerView.setAdapter(upcomingEventsAdapter);

        eventsRecyclerView = findViewById(R.id.eventsRecyclerView);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //set the adapter to the RecyclerView
        adapter = new EventAdapter(db, this);
        eventsRecyclerView.setAdapter(adapter);

        eventList = db.getAllEvents();
        Collections.reverse(eventList);
        adapter.setEvents(eventList);

        //populating the List with events that are due to happen in the next 7 days
        upEventList = db.getAllUpcomingEvents(todayDate(), threeDayDate());
        Collections.reverse(upEventList);
        upcomingEventsAdapter.setUpComingEvents(upEventList);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewEvent.newInstance().show(getSupportFragmentManager(), AddNewEvent.TAG);
            }
        });
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        eventList = db.getAllEvents();
        upEventList = db.getAllUpcomingEvents(todayDate(), threeDayDate());
        //this ensures that the most recent event is on top of the eventList
        Collections.reverse(eventList);
        adapter.setEvents(eventList);
        adapter.notifyDataSetChanged();

        Collections.reverse(upEventList);
        upcomingEventsAdapter.setUpComingEvents(upEventList);
        upcomingEventsAdapter.notifyDataSetChanged();

    }

    public String todayDate() {
        String todayDate = new SimpleDateFormat("yyyy/MM/dd", Locale.US).format(new Date());
        System.out.println(todayDate);
        return todayDate;
    }

    public String threeDayDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
        Calendar cDate = Calendar.getInstance();
        Date today = cDate.getTime();
        cDate.add(Calendar.DATE, 3);
        Date future = cDate.getTime();
        String futureDate = new SimpleDateFormat("yyyy/MM/dd", Locale.US).format(future);
        System.out.println(futureDate);

        return futureDate;
    }
}