package com.example.eventprototype;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.eventprototype.Db.DatabaseHandler;
import com.example.eventprototype.Model.EngagementModel;
import com.example.eventprototype.Model.EventModel;
import com.example.eventprototype.Model.UserModel;
import com.google.android.material.textfield.TextInputEditText;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddNewEvent extends DialogFragment {

    public static final String TAG = "ActionBottomDialog";

    private TextInputEditText eventTitle, eventStartTime, eventDate, eventLocation;
    private Button eventSaveBtn, eventImageBtn;
    private ImageView eventCoverPhoto;
    private ImageView eventCancelBtn;
    private DatabaseHandler db;
    private final Calendar myCalendar = Calendar.getInstance();
    private int hour;
    private int min;
    private Uri imageFilePath;
    private Bitmap imageToStore;
    private static final int PICK_IMAGE_REQUEST = 100;
    private TextView menuTitle;
    private ImageView backBtn;
    private ArrayList<UserModel> userList;


    //used to return object of AddNewEvent class so MainActivity can call the methods
    public static AddNewEvent newInstance() {
        return new AddNewEvent();
    }

    //savedInstanceState checks if this fragment exists in the memory
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_event, container, false);
        //re-adjust the keyboard when typing
        //getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //getView is used as it is a fragment
        //setting the xml components

        eventCoverPhoto = getView().findViewById(R.id.eventCoverPhoto);
        eventTitle = getView().findViewById(R.id.eventTitle);
        eventStartTime = getView().findViewById(R.id.eventStartTime);
        eventDate = getView().findViewById(R.id.eventDate);
        eventLocation = getView().findViewById(R.id.eventLocation);
        eventSaveBtn = getView().findViewById(R.id.eventSaveBtn);
        eventCancelBtn = getView().findViewById(R.id.eventCancelBtn);
        eventImageBtn = getView().findViewById(R.id.eventImageBtn);

        menuTitle = getView().findViewById(R.id.toolbar_heading_only);
        menuTitle.setText("New Event");

        /*
        TODO: fix the style of this class, not sure that a fragment will work
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddNewEvent.this, DashboardActivity.class);
                //TODO: pass intent of user back here
                startActivity(intent);
            }
        });
         */

        //pass the activity to the DatabaseHandler
        db = new DatabaseHandler(getActivity());
        db.openDatabase();

        /* blocking out this code until the add event is completed
        //checks if we are updating event or creating a new event
        //this would execute different queries in the db
        boolean isUpdate = false;
        //bundle gets data from our fragment
        final Bundle bundle = getArguments();
        //if some tasks have already been passed for updating
        if(bundle != null) {
            //trying to update a event and not create a new event
            isUpdate = true;
            String event = bundle.getString("event");
            newEventText.setText(event);
            if (event.length() > 0) {
                //whenever event is existing, it will have a teal colour
                newEventButton.setTextColor(ContextCompat.getColor(getContext(), R.color.teal_200));
            }
        }
         */

        //check if the xml id has been changed we will dull out the save button if blank task
        eventTitle.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkRequiredFields();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });

        eventStartTime.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkRequiredFields();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });

        eventDate.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkRequiredFields();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });

        eventLocation.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkRequiredFields();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });



        //onClick listener for when all fields are filled out
        eventSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pulls all the user inputs from the respective editable fields
                String title = eventTitle.getText().toString();
                String startTime = eventStartTime.getText().toString();
                String oldDate = eventDate.getText().toString();
                //converting the date to YYYY-MM-DD format before storage
                String newDate = null;
                try {
                    newDate = convertDateFormat(oldDate);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                String location = eventLocation.getText().toString();
                //initialise the Event object
                EventModel event = new EventModel();
                //setting the setters of the Event object to reflect in the RecyclerView adapter
                event.setEvent(title);
                event.setStartTime(startTime);
                event.setDate(newDate);
                event.setLocation(location);
                event.setStatus(0);
                //setting the bitmap of the image selected
                event.setEventCoverImage(imageToStore);
                //insert the created Event into the database
                db.insertEvent(event);
                //method that populates the engagement table for all users after a new event is created
                createEngagements();
                dismiss();



            }

        });

        //want to insert after the event is added to the eventList table


        eventCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        eventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, month);
                        myCalendar.set(Calendar.DAY_OF_MONTH, day);
                        updateLabel();
                    }
                };
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),R.style.DatePickerCustom, date, myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
                datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.themeSecondaryColor));
                datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.themeSecondaryColor));

                //new DatePickerDialog(getContext(),R.style.DatePickerCustom ,date, myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });


        eventStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMin) {
                        hour = selectedHour;
                        min = selectedMin;
                        eventStartTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, min));
                    }
                };
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), R.style.TimePickerCustom, time, hour , min, true);
                timePickerDialog.setTitle("Select Time");
                timePickerDialog.show();

                timePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.themeSecondaryColor));
                timePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.themeSecondaryColor));
            }
        });

        //on button click listener for when eventImageBtn is pressed
        eventImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //run the chooseCoverImage() method
                chooseCoverImage();
            }
        });
    }

    //intent that launches the file folder in the user's device
    public void chooseCoverImage() {
        //creates a new intent
        Intent objectIntent = new Intent();
        //MIME type set as image as we want to return an image from the intent
        objectIntent.setType("image/*");
        /*ACTION_GET_CONTENT is an implicit intent that allows the system to decide which is the best app to launch to fulfil this
          i.e. system launches "Files" app as a result of the implicit intent
         */
        objectIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(objectIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                imageFilePath = data.getData();
                imageToStore = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageFilePath);
                eventCoverPhoto.setImageBitmap(imageToStore);
            }
        }
        catch (Exception e) {

        }

    }

    //checks all EditText fields to see if everything is filled out, otherwise disables the save button
    public void checkRequiredFields() {
        if (!eventTitle.getText().toString().isEmpty() && !eventStartTime.getText().toString().isEmpty() && !eventDate.getText().toString().isEmpty() && !eventLocation.getText().toString().isEmpty()) {
            eventSaveBtn.setEnabled(true);
        }
        else {
            eventSaveBtn.setEnabled(false);
        }
    }

    //updates EditText field with the date that the user selected
    public void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        eventDate.setText(dateFormat.format(myCalendar.getTime()));
    }

    public String convertDateFormat(String oldDate) throws ParseException {
        SimpleDateFormat input = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat output = new SimpleDateFormat("yyyy/MM/dd");
        Date converted = input.parse(oldDate);
        String newDate = output.format(converted);
        System.out.println(newDate);
        return newDate;
    }

    public void createEngagements() {
        //we want to trace down the event that was just inserted
        List<EventModel> eventList = db.getAllEvents();
        //to get the most recently added Event object, we get the length of the list minus 1
        int listLength = eventList.size() - 1;
        //create instance of Engagement object
        EngagementModel newEngagement = new EngagementModel();
        //get all the users
        List<UserModel> userList = db.getAllUsers();
        //loop over all users in the system and add new Engagements for all users, upon a new event creation
        for (UserModel i : userList) {
            //accessing the recently inserted event's getters and setting the id information into Engagement setter
            newEngagement.setEventId(eventList.get(listLength).getId());
            //default is not joined activity
            newEngagement.setIsJoin(0);
            //accessing the user's id and setting it to the setter method
            newEngagement.setUserId(i.getId());
            //call method from DatabaseHandler to insert into Engagement table
            db.insertEngagement(newEngagement);
        }
    }

    //this abstract method is to ensure after updating the db, the RecyclerView is immediately updated
    @Override
    public void onDismiss(DialogInterface dialog) {
        Activity activity = getActivity();
        //DialogCloseListener is an interface in charge of updating RecyclerView
        if (activity instanceof  DialogCloseListener) {
            /*if the updating of events is called from an activity, we need to also run methods that
              update the RecyclerView
             */
            ((DialogCloseListener)activity).handleDialogClose(dialog);

        }
    }
}






















