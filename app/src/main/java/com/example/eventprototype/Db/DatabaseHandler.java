package com.example.eventprototype.Db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.metrics.Event;

import com.example.eventprototype.Model.EventModel;
import com.example.eventprototype.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class DatabaseHandler extends SQLiteOpenHelper {
    final private String unswDatabase= "jdbc:sqlite:unswDatabase.db";

    private static final int VERSION = 1;
    private static final String NAME = "EventListDatabase";
    private static final String EVENT_TABLE = "eventList";
    private static final String ID = "id";
    private static final String EVENT = "event";
    private static final String STATUS = "status";
    private static final String START_TIME = "eventStartTime";
    private static final String END_TIME= "eventFinishTime";
    private static final String LOCATION = "eventLocation";
    private int isStaff = 0;

    //SQL query to create table
    private static final String CREATE_EVENT_TABLE = "CREATE TABLE " + EVENT_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + EVENT + " TEXT, " + START_TIME + " TEXT, " + END_TIME + " TEXT, " + LOCATION + " TEXT, " + STATUS + " INTEGER)";
    //reference of the database
    private SQLiteDatabase db;

    public DatabaseHandler(Context context) {
        super(context, NAME, null, VERSION);
    }

    //Creating tables
    public void setUpDatabase() throws SQLException {
        // Connect to Database
        Connection conn = DriverManager.getConnection("jdbc:sqlite:unswDatabase.db");
        Statement st = conn.createStatement();

        // Create User with password, username
        String createUser = "CREATE TABLE IF NOT EXISTS USERS"
                + "(id INTEGER PRIMARY KEY AUTOINCREMENT"
                + ", username TEXT NOT NULL"
                + ", password TEXT NOT NULL"
                + ", isStaff INTEGER NOT NULL"
                + ");";
        st.execute(createUser);

        st.close();
        conn.close();
    }

    //insert users custom
    public void insertUsers(int id, String username, String password, int isStaff) throws SQLException{
        //create connection
        Connection conn = DriverManager.getConnection(unswDatabase);
        //create statement
        Statement st = conn.createStatement();

        //write the SQL query and the java code to insert all attributes
        PreparedStatement pSt = conn.prepareStatement(
                "INSERT OR IGNORE INTO USERS (id,username,password,isStaff) VALUES (?,?,?,?)"
        );

        pSt.setInt(1, id);
        pSt.setString(2, username);
        pSt.setString(3, password);
        pSt.setInt(4, isStaff);
        pSt.executeUpdate();

        st.close();
        conn.close();
    }

    public boolean login(String username, String password, boolean isStaff) throws SQLException {
        boolean userExists;

        Connection conn = DriverManager.getConnection(unswDatabase);
        PreparedStatement pst = conn.prepareStatement(
                "SELECT * FROM Users WHERE USERNAME = ? AND PASSWORD = ?"
        );
        pst.setString(1, username);
        pst.setString(2, password);
        ResultSet rs = pst.executeQuery();

        // Check if user exists - if so return true, else return false
        int rowCount = 0;
        while(rs.next()) {
            rowCount++;
        }

        if(rowCount > 0) {
            userExists = true;
        } else {
            userExists = false;
        }
        return userExists;
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
