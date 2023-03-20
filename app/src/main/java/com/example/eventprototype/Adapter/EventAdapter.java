package com.example.eventprototype.Adapter;

import android.content.Context;
import android.media.metrics.Event;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventprototype.AddNewEvent;
import com.example.eventprototype.Db.DatabaseHandler;
import com.example.eventprototype.MainActivity;
import com.example.eventprototype.Model.EventModel;
import com.example.eventprototype.R;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private List<EventModel> eventList;
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

    }

    @Override
    public int getItemCount() {
        //tells Recyclerview how many items to return
        return eventList.size();
    }

    private boolean toBoolean(int n) {
        if (n == 0) {
            return false;
        }
        else {
            return true;
        }
    }

    //method to set the eventList passed from MainActivity containing the events to this eventList
    public void setEvents(List<EventModel> eventList) {
        this.eventList = eventList;
        notifyDataSetChanged();
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

        ViewHolder(View view) {
            super(view);
            eventStartTime = view.findViewById(R.id.eventStartTime);
            eventDate = view.findViewById(R.id.eventDate);
            eventLocation = view.findViewById(R.id.eventLocation);
            eventTitle = view.findViewById(R.id.eventTitle);
            eventImage = view.findViewById(R.id.eventImage);
            //sets the corner radius to rounded as in rounded_rectangle.xml
            eventImage.setClipToOutline(true);

        }

    }


}
