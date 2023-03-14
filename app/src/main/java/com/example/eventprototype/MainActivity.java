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
import com.example.eventprototype.Db.DatabaseHandler;
import com.example.eventprototype.Model.EventModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListener{

    private RecyclerView eventsRecyclerView;
    private EventAdapter adapter;
    private List<EventModel> eventList;
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

        eventsRecyclerView = findViewById(R.id.eventsRecyclerView);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //set the adapter to the RecyclerView
        adapter = new EventAdapter(db, this);
        eventsRecyclerView.setAdapter(adapter);

        eventList = db.getAllEvents();
        Collections.reverse(eventList);
        adapter.setEvents(eventList);

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
        //this ensures that the most recent event is on top of the eventList
        Collections.reverse(eventList);
        adapter.setEvents(eventList);
        adapter.notifyDataSetChanged();

    }
}