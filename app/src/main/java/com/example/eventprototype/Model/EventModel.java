package com.example.eventprototype.Model;

import android.graphics.Bitmap;

public class EventModel {
    private int id, status;
    //"event" is the name of the event in SQLITE
    private int isJoin;
    private int userId;
    private String event;
    private String location;
    private String startTime;
    private String date;
    private Bitmap eventCoverImage;


    public Bitmap getEventCoverImage() {
        return eventCoverImage;
    }

    public void setEventCoverImage(Bitmap eventCoverImage) {
        this.eventCoverImage = eventCoverImage;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public int getIsJoin() {
        return isJoin;
    }

    public void setIsJoin(int isJoin) {
        this.isJoin = isJoin;
    }
}
