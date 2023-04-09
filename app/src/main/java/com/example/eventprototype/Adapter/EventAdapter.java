package com.example.eventprototype.Adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.media.metrics.Event;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventprototype.AddNewEvent;
import com.example.eventprototype.Db.DatabaseHandler;
import com.example.eventprototype.MainActivity;
import com.example.eventprototype.Model.EventModel;
import com.example.eventprototype.Model.UserModel;
import com.example.eventprototype.R;
import com.google.android.material.button.MaterialButton;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private List<EventModel> eventList;
    private List<UserModel> currentUser;
    private MainActivity activity;
    private DatabaseHandler db;

    //EventAdapter constructor class
    public EventAdapter(DatabaseHandler db, MainActivity activity) {
        this.db = db;
        this.activity = activity;
    }

    @NonNull
    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.ViewHolder holder, int position) {
        //open database
        db.openDatabase();
        //list of EventModel objects containing items aka events based on the position
        EventModel item = eventList.get(position);
        //each event you get from the holder by referring the EventModel and its variables
        holder.eventTitle.setText(item.getEvent());
        holder.eventLocation.setText(item.getLocation());
        holder.eventStartTime.setText(item.getStartTime());
        String oldDate = item.getDate();
        String convertedDate = null;
        try {
            convertedDate = convertDateFormat(oldDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        holder.eventDate.setText(convertedDate);
        holder.eventImage.setImageBitmap(item.getEventCoverImage());
        int joinStatus = item.getIsJoin();

        //check to see if user has joined the event or not and setting the buttons accordingly
        if (joinStatus == 0) {
            holder.eventJoin.setText("Join");
            holder.eventJoin.setTextColor(getContext().getResources().getColor(R.color.joinColor));
            holder.eventJoin.setBackgroundColor(getContext().getResources().getColor(R.color.white));
            holder.eventJoin.setStrokeColor(ColorStateList.valueOf(getContext().getResources().getColor(R.color.joinColor)));
        }
        else if (joinStatus == 1) {
            holder.eventJoin.setText("Joined");
            holder.eventJoin.setTextColor(getContext().getResources().getColor(R.color.white));
            holder.eventJoin.setBackgroundColor(getContext().getResources().getColor(R.color.joinColor));
        }

        //onClick listener that updates the db isJoin status on the Engagement table
        holder.eventJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //checking if the user has joined or not joined the event
                //currently user has not joined this event and will need to update db to joined and update the button
                if (joinStatus == 0) {
                    holder.eventJoin.setText("Joined");
                    holder.eventJoin.setTextColor(view.getResources().getColor(R.color.white));
                    holder.eventJoin.setBackgroundColor(view.getResources().getColor(R.color.joinColor));
                    db.updateEngagement(currentUser.get(0).getId(), item.getId(), 1);
                    //reload list
                    eventList = db.getAllUserEvents(currentUser.get(0).getId());
                    //reverse list order
                    //Collections.reverse(eventList);
                    Toast.makeText(view.getContext(), "ITEM PRESSED = " + item.getIsJoin(), Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                }
                else if (joinStatus == 1) {
                    holder.eventJoin.setText("Join");
                    holder.eventJoin.setTextColor(view.getResources().getColor(R.color.joinColor));
                    holder.eventJoin.setBackgroundColor(view.getResources().getColor(R.color.white));
                    holder.eventJoin.setStrokeColor(ColorStateList.valueOf(view.getResources().getColor(R.color.joinColor)));
                    db.updateEngagement(currentUser.get(0).getId(), item.getId(), 0);
                    //reload list
                    eventList = db.getAllUserEvents(currentUser.get(0).getId());
                    //reverse list order
                    //Collections.reverse(eventList);
                    Toast.makeText(view.getContext(), "ITEM PRESSED = " + item.getIsJoin(), Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                }


            }
        });

    }

    @Override
    public int getItemCount() {
        //tells Recyclerview how many items to return
        return eventList.size();
    }



    //method to set the eventList passed from MainActivity containing the events to this eventList
    public void setEvents(List<EventModel> eventList) {
        this.eventList = eventList;
        notifyDataSetChanged();
    }

    //method to pass current user from MainActivity to EventAdapter
    public void setCurrentUser(List<UserModel> currentUser) {
        this.currentUser = currentUser;

    }

    //function to convert from yyyy/MM/dd format of the db to dd/MM/yyyy to display
    public String convertDateFormat(String oldDate) throws ParseException {
        SimpleDateFormat input = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");
        Date converted = input.parse(oldDate);
        String newDate = output.format(converted);
        return newDate;
    }


    public void editItem(int position) {
        EventModel event = eventList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", event.getId());
        bundle.putString("event", event.getEvent());
        AddNewEvent fragment = new AddNewEvent();
        //used to pass bundle
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewEvent.TAG);
    }

    public void deleteItem(int position) {
        EventModel event = eventList.get(position);
        //will delete the event from the database
        db.deleteEvent(event.getId());
        //remove from the RecyclerView and update RecyclerView
        eventList.remove(position);
        notifyItemRemoved(position);
    }

    public Context getContext() {
        return activity;
    }




    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView eventStartTime;
        TextView eventDate;
        TextView eventLocation;
        TextView eventTitle;
        ImageView eventImage;
        MaterialButton eventJoin;


        ViewHolder(View view) {
            super(view);
            eventStartTime = view.findViewById(R.id.eventStartTime);
            eventDate = view.findViewById(R.id.eventDate);
            eventLocation = view.findViewById(R.id.eventLocation);
            eventTitle = view.findViewById(R.id.eventTitle);
            eventImage = view.findViewById(R.id.eventImage);
            //sets the corner radius to rounded as in rounded_rectangle.xml
            eventImage.setClipToOutline(true);
            eventJoin = view.findViewById(R.id.eventJoin);



        }

    }


}
