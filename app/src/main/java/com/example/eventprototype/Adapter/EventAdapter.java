package com.example.eventprototype.Adapter;

import android.content.Context;
import android.media.metrics.Event;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventprototype.AddNewEvent;
import com.example.eventprototype.Db.DatabaseHandler;
import com.example.eventprototype.MainActivity;
import com.example.eventprototype.Model.EventModel;
import com.example.eventprototype.R;

import org.w3c.dom.Text;

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
        holder.eventFinishTime.setText(item.getEndTime());

        /* phasing out checkBox feature until required - every time you check or uncheck box, got to update database
        holder.eventCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    db.updateStatus(item.getId(), 1);
                }
                else {
                    db.updateStatus(item.getId(), 0);
                }
            }
        });
         */
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
        TextView eventFinishTime;
        TextView eventLocation;
        TextView eventTitle;

        ViewHolder(View view) {
            super(view);
            eventStartTime = view.findViewById(R.id.eventStartTime);
            eventFinishTime = view.findViewById(R.id.eventFinishTime);
            eventLocation = view.findViewById(R.id.eventLocation);
            eventTitle = view.findViewById(R.id.eventTitle);
        }

    }
}
