package com.example.eventprototype;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eventprototype.Adapter.EventAdapter;
import com.example.eventprototype.Adapter.UpcomingEventsAdapter;
import com.example.eventprototype.Db.DatabaseHandler;
import com.example.eventprototype.Model.EngagementModel;
import com.example.eventprototype.Model.EventModel;
import com.example.eventprototype.Model.UserModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MainActivity extends AppCompatActivity implements DialogCloseListener{

    private RecyclerView eventsRecyclerView, upcomingEventsRecyclerView;
    private EventAdapter adapter;
    private UpcomingEventsAdapter upcomingEventsAdapter;
    private List<EventModel> eventList, upEventList;
    private ArrayList<UserModel> currentUser;
    private DatabaseHandler db;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        //set the icon as selected
        bottomNavigationView.setSelectedItemId(R.id.events);

        //get the intent passed from either profile or dashboard
        currentUser = (ArrayList<UserModel>) getIntent().getSerializableExtra("currentUser");
        System.out.println(currentUser.get(0).getUsername());
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.dashboard:
                        Intent intent = new Intent(MainActivity.this, DashboardActivity.class );
                        intent.putExtra("currentUser", currentUser);
                        startActivity(intent);
                        return true;

                    case R.id.profile:
                        intent = new Intent(MainActivity.this, ProfileActivity.class );
                        intent.putExtra("currentUser", currentUser);
                        startActivity(intent);
                        return true;
                }
                return false;
            }
        });


        Toast.makeText(MainActivity.this, "User logged in: " + currentUser.get(0).getUsername(), Toast.LENGTH_SHORT).show();
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


        eventList = db.getAllUserEvents(currentUser.get(0).getId());
        //Collections.reverse(eventList);
        adapter.setCurrentUser(currentUser);
        adapter.setEvents(eventList);

        //populating the List with events that are due to happen in the next 7 days
        upEventList = db.getAllUpcomingEvents(todayDate(), threeDayDate());
        //Collections.reverse(upEventList);
        upcomingEventsAdapter.setUpComingEvents(upEventList);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewEvent.newInstance().show(getSupportFragmentManager(), AddNewEvent.TAG);
            }
        });

        //ItemTouchHelper class
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        //attach itemTouchHelper to the RecyclerView
        itemTouchHelper.attachToRecyclerView(eventsRecyclerView);


    }


    //helper class for the user swipe gestures that takes in two variables - drag direction and swipe direction
    //takes in swipe directions from both sides, left->right and right->left
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        //onMove is only used if you want to move rows in your RecyclerView
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        //method takes in viewHolder and direction of the swipe
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            //get position of the row
            int position = viewHolder.getAdapterPosition();

            switch (direction) {
                //swipe from right to left
                case ItemTouchHelper.LEFT:
                    //variable storing the deleted EventModel object
                    EventModel deletedEvent = eventList.get(position);
                    //variable storing the deleted EventModel, event name
                    String deletedEventName = eventList.get(position).getEvent();
                    //remove the event from the viewHolder and eventList
                    eventList.remove(position);
                    //TODO: implement function that also checks if the removed event is part of the upComingEvents list, if so remove it from there too
                    //call method to remove the engagements from the database for all users
                    deleteEventEngagements(deletedEvent.getId());
                    //call method to remove the event from the database
                    db.deleteEvent(deletedEvent.getId());
                    adapter.notifyItemRemoved(position);
                    //refresh the listing that EventAdapter get so that the adapter class knows there is a change
                    eventList = db.getAllUserEvents(currentUser.get(0).getId());
                    adapter.setEvents(eventList);
                    Snackbar.make(eventsRecyclerView, deletedEventName, Snackbar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener() {
                                //undo button that brings back the deleted event
                                @Override
                                public void onClick(View view) {
                                    //adding the event back to the eventList
                                    eventList.add(position, deletedEvent);
                                    //adding the event engagements back to the Engagement table in db for all users
                                    restoreEngagements(deletedEvent.getId());
                                    //adding the event back to Event table where it was before
                                    db.insertDeletedEvent(deletedEvent);
                                    //refresh the listing that EventAdapter get so that the adapter class knows theres a change
                                    eventList = db.getAllUserEvents(currentUser.get(0).getId());
                                    adapter.setEvents(eventList);
                                    adapter.notifyDataSetChanged();
                        }
                    }).show();
                    break;

                //swipe from left to right
                case ItemTouchHelper.RIGHT:
                    EditEvent editEvent = new EditEvent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("clickedRow", eventList.get(position));
                    editEvent.setArguments(bundle);
                    editEvent.show((MainActivity.this).getSupportFragmentManager(), editEvent.getTag());
                    //EditEvent.newInstance().show(getSupportFragmentManager(), EditEvent.TAG);
                    break;
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.green))
                    .addSwipeRightActionIcon(R.drawable.baseline_edit_24)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.red))
                    .addSwipeLeftActionIcon(R.drawable.baseline_delete_24)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        eventList = db.getAllUserEvents(currentUser.get(0).getId());
        upEventList = db.getAllUpcomingEvents(todayDate(), threeDayDate());
        //this ensures that the most recent event is on top of the eventList
        //Collections.reverse(eventList);
        adapter.setEvents(eventList);
        adapter.notifyDataSetChanged();

        //Collections.reverse(upEventList);
        upcomingEventsAdapter.setUpComingEvents(upEventList);
        upcomingEventsAdapter.notifyDataSetChanged();
    }

    //method to delete all the engagements for all users for the deleted event given foreign key constraints
    public void deleteEventEngagements(int eventId) {
        //get all the users
        List<UserModel> userList = db.getAllUsers();
        //loop through all the users and delete the engagement connected to the event for all users
        for (UserModel i : userList) {
            //get the userIds from each user
            int userId = i.getId();
            db.deleteEngagement(userId, eventId);
        }
    }

    //method for undoing the deleted engagements and adding all engagements back for the deleted event in the database
    public void restoreEngagements(int eventId) {
        //create instance of Engagement object
        EngagementModel newEngagement = new EngagementModel();
        //get all the users
        List<UserModel> userList = db.getAllUsers();
        //loop over all users in the system and add new Engagements for all users, upon a new event creation
        for (UserModel i : userList) {
            //accessing the recently deleted event's getters and setting the id information into Engagement setter
            newEngagement.setEventId(eventId);
            //default is not joined activity
            newEngagement.setIsJoin(0);
            //accessing the user's id for the current user to re-add the engagement for
            newEngagement.setUserId(i.getId());
            //call method from DatabaseHandler to insert into Engagement table
            db.insertEngagement(newEngagement);
        }
    }

    //method to get today's date
    public String todayDate() {
        String todayDate = new SimpleDateFormat("yyyy/MM/dd", Locale.US).format(new Date());
        System.out.println(todayDate);
        return todayDate;
    }

    //get the date 3 days in the future
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