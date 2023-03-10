package com.example.eventprototype.Db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.metrics.Event;

import com.example.eventprototype.Model.EventModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NAME = "EventListDatabase";
    private static final String EVENT_TABLE = "eventList";
    private static final String ID = "id";
    private static final String EVENT = "event";
    private static final String STATUS = "status";
    private static final String START_TIME = "eventStartTime";
    private static final String END_TIME= "eventFinishTime";
    private static final String LOCATION = "eventLocation";

    //SQL query to create table
    private static final String CREATE_EVENT_TABLE = "CREATE TABLE " + EVENT_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + EVENT + " TEXT, " + START_TIME + " TEXT, " + END_TIME + " TEXT, " + LOCATION + " TEXT, " + STATUS + " INTEGER)";
    //reference of the database
    private SQLiteDatabase db;

    public DatabaseHandler(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + EVENT_TABLE);
        //query to create a new table to ensure there is a table when the db is created
        db.execSQL(CREATE_EVENT_TABLE);

    }

    //mandatory for all SQLiteOpenHelper handler classes
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //drop the older tables
        db.execSQL("DROP TABLE IF EXISTS " + EVENT_TABLE);
        //create the tables again
        onCreate(db);
    }

    //method called in the MainActivity
    public void openDatabase() {
        //opens the db as writeable
        db = this.getWritableDatabase();
    }

    public void insertEvent(EventModel event) {
        //ContentValues is a key/value store that inserts data into a row of a table
        ContentValues cv = new ContentValues();
        //no ID needed as that is already auto-incremented
        cv.put(EVENT, event.getEvent());
        cv.put(START_TIME, event.getStartTime());
        cv.put(END_TIME, event.getEndTime());
        cv.put(LOCATION, event.getLocation());
        cv.put(STATUS, 0);
        //insert the above info into the table below
        db.insert(EVENT_TABLE, null, cv);
    }

    //get all events from the db and store it in our List eventList
    @SuppressLint("Range")
    public List<EventModel> getAllEvents() {
        List<EventModel> eventList = new ArrayList<>();
        Cursor cursor = null;
        //will ensure the database doesn't get corrupted should we read/write data and accidentally close app
        db.beginTransaction();
        try {
            //return all the rows from the db without any criteria
            //cursor is the current row being pointed to in the SQL result
            cursor = db.query(EVENT_TABLE, null, null, null, null, null, null, null);
            if (cursor != null) {
                //moveToFirst will return false if the first line pointed to by the cursor is empty
                if (cursor.moveToFirst()) {
                    do {
                        EventModel event = new EventModel();
                        event.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                        event.setEvent(cursor.getString(cursor.getColumnIndex(EVENT)));
                        event.setStartTime(cursor.getString(cursor.getColumnIndex(START_TIME)));
                        event.setEndTime(cursor.getString(cursor.getColumnIndex(END_TIME)));
                        event.setLocation(cursor.getString(cursor.getColumnIndex(LOCATION)));
                        event.setStatus(cursor.getInt(cursor.getColumnIndex(STATUS)));
                        //adds event object to the eventList
                        eventList.add(event);
                    }while (cursor.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            cursor.close();
        }
        return eventList;
    }

    //method for updating status
    public void updateStatus(int id, int status) {
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        //? is for formatting purposes
        //convert ID to a string for db match on ID note: that ID was denoted as a String
        db.update(EVENT_TABLE, cv, ID + "=?", new String[] {String.valueOf(id)});
    }

    //method for updating event
    public void updateEvent(int id, String event) {
        ContentValues cv = new ContentValues();
        //cv.put takes in the column to update and the new value
        cv.put(EVENT, event);
        db.update(EVENT_TABLE, cv, ID + "=?", new String[] {String.valueOf(id)});
    }

    public void updateStartTime(int id, String startTime) {
        ContentValues cv = new ContentValues();
        cv.put(START_TIME, startTime);
        db.update(EVENT_TABLE, cv, ID + "=?", new String[] {String.valueOf(id)});
    }

    public void updateEndTime(int id, String endTime) {
        ContentValues cv = new ContentValues();
        cv.put(END_TIME, endTime);
        db.update(EVENT_TABLE, cv, ID + "=?", new String[] {String.valueOf(id)});
    }

    public void updateLocation(int id, String location) {
        ContentValues cv = new ContentValues();
        cv.put(LOCATION, location);
        db.update(EVENT_TABLE, cv, ID + "=?", new String[] {String.valueOf(id)});
    }

    //method for deleting event
    public void deleteEvent(int id) {
        db.delete(EVENT_TABLE, ID + "=?", new String[] {String.valueOf(id)});
    }
}
