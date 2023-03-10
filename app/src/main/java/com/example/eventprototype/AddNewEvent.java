package com.example.eventprototype;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.eventprototype.Db.DatabaseHandler;
import com.example.eventprototype.Model.EventModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class AddNewEvent extends BottomSheetDialogFragment {

    public static final String TAG = "ActionBottomDialog";

    private TextInputEditText eventTitle, eventStartTime, eventEndTime, eventLocation;
    private Button eventSaveBtn;
    private DatabaseHandler db;

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
        eventTitle = getView().findViewById(R.id.eventTitle);
        eventStartTime = getView().findViewById(R.id.eventStartTime);
        eventEndTime = getView().findViewById(R.id.eventEndTime);
        eventLocation = getView().findViewById(R.id.eventLocation);
        eventSaveBtn = getView().findViewById(R.id.eventSaveBtn);

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
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            //check whether the event is empty or not
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //disable save button is empty
                if(s.toString().equals("")) {
                    eventSaveBtn.setEnabled(false);
                    eventSaveBtn.setTextColor(Color.GRAY);
                }
                //otherwise enable save button
                else {
                    eventSaveBtn.setEnabled(true);
                    eventSaveBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.teal_200));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //check if start time is populated
        eventStartTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            //check whether the event is empty or not
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //disable save button is empty
                if(s.toString().equals("")) {
                    eventSaveBtn.setEnabled(false);
                    eventSaveBtn.setTextColor(Color.GRAY);
                }
                //otherwise enable save button
                else {
                    eventSaveBtn.setEnabled(true);
                    eventSaveBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.teal_200));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //check if event end time is populated
        eventEndTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            //check whether the event is empty or not
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //disable save button is empty
                if(s.toString().equals("")) {
                    eventSaveBtn.setEnabled(false);
                    eventSaveBtn.setTextColor(Color.GRAY);
                }
                //otherwise enable save button
                else {
                    eventSaveBtn.setEnabled(true);
                    eventSaveBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.teal_200));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //check if location is populated
        eventLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            //check whether the event is empty or not
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //disable save button is empty
                if(s.toString().equals("")) {
                    eventSaveBtn.setEnabled(false);
                    eventSaveBtn.setTextColor(Color.GRAY);
                }
                //otherwise enable save button
                else {
                    eventSaveBtn.setEnabled(true);
                    eventSaveBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.teal_200));
                }
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
                String endTime = eventEndTime.getText().toString();
                String location = eventLocation.getText().toString();
                //initialise the Event object
                EventModel event = new EventModel();
                //setting the setters of the Event object to reflect in the RecyclerView adapter
                event.setEvent(title);
                event.setStartTime(startTime);
                event.setEndTime(endTime);
                event.setLocation(location);
                event.setStatus(0);
                //insert the created Event into the database
                db.insertEvent(event);
                dismiss();
            }

        });

        /* blocked out until update function is implemented
        final boolean finalIsUpdate = isUpdate;
        newEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if we are trying to update an already existing event or create a new event
                String text = newEventText.getText().toString();
                if (finalIsUpdate) {
                    db.updateEvent(bundle.getInt("id"), text);
                }
                else {
                    EventModel event = new EventModel();
                    event.setEvent(text);
                    event.setStatus(0);
                    db.insertEvent(event);
                }
                dismiss();
            }
        });

         */
    }
    //this method is to ensure after updating the db, the RecyclerView is immediately updated
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






















