package com.example.eventprototype.Db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.metrics.Event;
import android.widget.Toast;

import com.example.eventprototype.Model.EventModel;
import com.example.eventprototype.Model.UserModel;

import java.io.ByteArrayOutputStream;
import java.net.ConnectException;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NAME = "EventListDatabase";
    private static final String EVENT_TABLE = "eventList";
    private static final String EVENT_ID = "eventId";
    private static final String EVENT = "event";
    private static final String STATUS = "status";
    private static final String DATE = "eventDate";
    private static final String IMAGE = "image";
    private static final String START_TIME = "eventStartTime";
    private static final String LOCATION = "eventLocation";
    private static final String USER_TABLE = "userList";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String STAFF = "isStaff";
    private static final String USER_ID = "userId";
    private static final String ENGAGEMENT_TABLE = "engagement";
    private static final String ENGAGEMENT_ID = "engagementId";
    private static final String ISJOIN = "isJoin";


    //SQL query to create engagement table
    private static final String CREATE_ENGAGEMENT_TABLE = "CREATE TABLE "
            + ENGAGEMENT_TABLE + "("
            + ENGAGEMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ISJOIN + " INTEGER, "
            + USER_ID + " INTEGER, "
            + EVENT_ID + " INTEGER, "
            + "FOREIGN KEY" + "(" + USER_ID + ")" + " REFERENCES " + USER_TABLE + "(" + USER_ID + "),"
            + " FOREIGN KEY" + "(" + EVENT_ID + ")" + " REFERENCES " + EVENT_TABLE + "(" + EVENT_ID + "))";

    //SQL query to create events table
    private static final String CREATE_EVENT_TABLE = "CREATE TABLE "
            + EVENT_TABLE + "("
            + EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + EVENT + " TEXT, "
            + START_TIME + " TEXT, "
            + DATE + " TEXT, "
            + LOCATION + " TEXT, "
            + STATUS + " INTEGER, "
            + IMAGE + " BLOB, "
            + USER_ID + " INTEGER, "
            + "FOREIGN KEY" + "(" + USER_ID + ")" + " REFERENCES " + USER_TABLE + "(" + USER_ID + "))";

    //SQL query to create users table
    private static final String CREATE_USERS_TABLE = "CREATE TABLE "
            + USER_TABLE + "("
            + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + USERNAME + " TEXT, "
            + PASSWORD + " TEXT, "
            + STAFF + " INTEGER)";
    private SQLiteDatabase db;

    private ByteArrayOutputStream byteArrayOutputStream;
    private byte[] imageInBytes;
    public DatabaseHandler(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + ENGAGEMENT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + EVENT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        //query to create the users table
        db.execSQL(CREATE_USERS_TABLE);
        //query to create the events table
        db.execSQL(CREATE_EVENT_TABLE);
        //query to create the engagement table
        db.execSQL(CREATE_ENGAGEMENT_TABLE);


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

    public void insertUser(UserModel user) {
        ContentValues cv = new ContentValues();
        //no ID needed as that is already auto-incremented
        cv.put(USERNAME, user.getUsername());
        cv.put(PASSWORD, user.getPassword());
        cv.put(STAFF, user.getIsStaff());
        //insert the above info into the USERS table
        db.insert(USER_TABLE, null, cv);
    }

    public void insertEvent(EventModel event) {
        //ContentValues is a key/value store that inserts data into a row of a table
        ContentValues cv = new ContentValues();
        //no ID needed as that is already auto-incremented
        cv.put(EVENT, event.getEvent());
        cv.put(START_TIME, event.getStartTime());
        cv.put(DATE, event.getDate());
        cv.put(LOCATION, event.getLocation());
        cv.put(STATUS, 0);
        /*prepping for the image insert as setters are storing image as a bitmap but we have to convert
          to bytes before storing
         */
        Bitmap imageToStoreBitmap = event.getEventCoverImage();
        byteArrayOutputStream = new ByteArrayOutputStream();
        imageToStoreBitmap.compress(Bitmap.CompressFormat.JPEG,100, byteArrayOutputStream);
        //convert image to an array of bytes for storage in database
        imageInBytes = byteArrayOutputStream.toByteArray();
        //bytes to be stored and passed in ContentValues
        cv.put(IMAGE, imageInBytes);
        //insert the above info into the table below
        db.insert(EVENT_TABLE, null, cv);
    }

    @SuppressLint("Range")
    public List<UserModel> getUser(String username, String password) {
        List<UserModel> userList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + USER_TABLE +
                " WHERE " + USERNAME + " = ? AND " + PASSWORD + " = ? ", new String[]{username, password});
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    UserModel user = new UserModel();
                    user.setId(cursor.getInt(cursor.getColumnIndex(USER_ID)));
                    user.setUsername(cursor.getString(cursor.getColumnIndex(USERNAME)));
                    user.setPassword(cursor.getString(cursor.getColumnIndex(PASSWORD)));
                    user.setIsStaff(cursor.getInt(cursor.getColumnIndex(STAFF)));
                    //returns the user information should it have the correct login details
                    userList.add(user);

                } while (cursor.moveToNext());
            }
        }
        return userList;
    }

    @SuppressLint("Range")
    public List<EventModel> getAllUpcomingEvents(String startDate, String endDate) {
        List<EventModel> upEventList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + EVENT_TABLE +
                " WHERE " + DATE +
                " BETWEEN ? AND ?", new String[]{startDate, endDate});
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    EventModel upComingEvent = new EventModel();
                    upComingEvent.setId(cursor.getInt(cursor.getColumnIndex(EVENT_ID)));
                    upComingEvent.setEvent(cursor.getString(cursor.getColumnIndex(EVENT)));
                    upComingEvent.setStartTime(cursor.getString(cursor.getColumnIndex(START_TIME)));
                    upComingEvent.setDate(cursor.getString(cursor.getColumnIndex(DATE)));
                    upComingEvent.setLocation(cursor.getString(cursor.getColumnIndex(LOCATION)));
                    upComingEvent.setStatus(cursor.getInt(cursor.getColumnIndex(STATUS)));
                    //convert the stored db images into Bitmap as they are stored as bytes
                    byte [] dbBytesImage = cursor.getBlob(cursor.getColumnIndex(IMAGE));
                    Bitmap objectBitmap = BitmapFactory.decodeByteArray(dbBytesImage, 0, dbBytesImage.length);
                    //setting the getters/setters
                    upComingEvent.setEventCoverImage(objectBitmap);
                    //adds event object to the eventList
                    upEventList.add(upComingEvent);


                } while (cursor.moveToNext());
            }
        }
        return upEventList;
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
                        event.setId(cursor.getInt(cursor.getColumnIndex(EVENT_ID)));
                        event.setEvent(cursor.getString(cursor.getColumnIndex(EVENT)));
                        event.setStartTime(cursor.getString(cursor.getColumnIndex(START_TIME)));
                        event.setDate(cursor.getString(cursor.getColumnIndex(DATE)));
                        event.setLocation(cursor.getString(cursor.getColumnIndex(LOCATION)));
                        event.setStatus(cursor.getInt(cursor.getColumnIndex(STATUS)));
                        //convert the stored db images into Bitmap as they are stored as bytes
                        byte [] dbBytesImage = cursor.getBlob(cursor.getColumnIndex(IMAGE));
                        Bitmap objectBitmap = BitmapFactory.decodeByteArray(dbBytesImage, 0, dbBytesImage.length);
                        //setting the getters/setters
                        event.setEventCoverImage(objectBitmap);
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

    /*
    public void storeImage(EventModel eventModel) {
        try {
            db = this.getWritableDatabase();
            Bitmap imageToStoreBitmap = eventModel.getEventCoverImage();
            byteArrayOutputStream = new ByteArrayOutputStream();
            imageToStoreBitmap.compress(Bitmap.CompressFormat.JPEG,100, byteArrayOutputStream);
            //convert image to an array of bytes for storage in database
            imageInBytes = byteArrayOutputStream.toByteArray();
            ContentValues cv = new ContentValues();
            cv.put(IMAGE, imageInBytes);
            long checkIfQueryRuns = db.insert(EVENT_TABLE, null, cv);
            if(checkIfQueryRuns != 0) {
                System.out.println("Data added into our db table");
                //db.close()
            }
            else {
                System.out.println("Data add failed");
            }

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

     */



    //method for updating status
    public void updateStatus(int id, int status) {
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        //? is for formatting purposes
        //convert ID to a string for db match on ID note: that ID was denoted as a String
        db.update(EVENT_TABLE, cv, EVENT_ID + "=?", new String[] {String.valueOf(id)});
    }

    //method for updating event
    public void updateEvent(int id, String event) {
        ContentValues cv = new ContentValues();
        //cv.put takes in the column to update and the new value
        cv.put(EVENT, event);
        db.update(EVENT_TABLE, cv, EVENT_ID + "=?", new String[] {String.valueOf(id)});
    }

    public void updateStartTime(int id, String startTime) {
        ContentValues cv = new ContentValues();
        cv.put(START_TIME, startTime);
        db.update(EVENT_TABLE, cv, EVENT_ID + "=?", new String[] {String.valueOf(id)});
    }

    public void updateDate(int id, String endTime) {
        ContentValues cv = new ContentValues();
        cv.put(DATE, endTime);
        db.update(EVENT_TABLE, cv, EVENT_ID + "=?", new String[] {String.valueOf(id)});
    }

    public void updateLocation(int id, String location) {
        ContentValues cv = new ContentValues();
        cv.put(LOCATION, location);
        db.update(EVENT_TABLE, cv, EVENT_ID + "=?", new String[] {String.valueOf(id)});
    }

    //method for deleting event
    public void deleteEvent(int id) {
        db.delete(EVENT_TABLE, EVENT_ID + "=?", new String[] {String.valueOf(id)});
    }


}
