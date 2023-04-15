package com.example.eventprototype.Model;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

@SuppressLint("ParcelCreator")
public class EventModel implements Serializable, Parcelable {
    private int id, status;
    //"event" is the name of the event in SQLITE
    private int isJoin;
    private int userId;
    private String event;
    private String location;
    private String startTime;
    private String date;
    private String faculty;

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    private String desc;
    private int budget;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {

    }
}
