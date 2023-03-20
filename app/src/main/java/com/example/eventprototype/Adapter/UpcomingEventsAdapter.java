package com.example.eventprototype.Adapter;

import android.media.Image;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventprototype.Db.DatabaseHandler;
import com.example.eventprototype.MainActivity;
import com.example.eventprototype.Model.EventModel;
import com.example.eventprototype.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UpcomingEventsAdapter extends RecyclerView.Adapter<UpcomingEventsAdapter.ViewHolder> {

    private List<EventModel> upEventList;
    private MainActivity activity;
    private DatabaseHandler db;


    public UpcomingEventsAdapter(DatabaseHandler db, MainActivity activity) {
        this.db = db;
        this.activity = activity;
    }

    @NonNull
    @Override
    public UpcomingEventsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.upcoming_event_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UpcomingEventsAdapter.ViewHolder holder, int position) {
        db.openDatabase();
        EventModel item = upEventList.get(position);
        holder.upEventImage.setImageBitmap(item.getEventCoverImage());
        String oldDate = item.getDate();
        String newDate = null;
        try {
            newDate = convertDateFormat(oldDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        holder.upEventDate.setText(newDate);
        holder.upEventTitle.setText(item.getEvent());
        holder.upEventTime.setText(item.getStartTime());
    }

    @Override
    public int getItemCount() {
        return upEventList.size();
    }

    public void setUpComingEvents(List<EventModel> eventList) {
        this.upEventList = eventList;
        notifyDataSetChanged();
    }

    //function to convert from yyyy/MM/dd format of the db to dd MONTH yyyy to display
    public String convertDateFormat(String oldDate) throws ParseException {
        SimpleDateFormat input = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat output = new SimpleDateFormat("dd LLLL yyyy");
        Date converted = input.parse(oldDate);
        String newDate = output.format(converted);
        return newDate;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView upEventImage;
        TextView upEventDate, upEventTitle, upEventTime;

        public ViewHolder(@NonNull View view) {
            super(view);
            upEventImage = view.findViewById(R.id.upEventImage);
            upEventDate = view.findViewById(R.id.upEventDate);
            upEventTitle = view.findViewById(R.id.upEventTitle);
            upEventTime = view.findViewById(R.id.upEventTime);
        }
    }
}
