package com.example.eventprototype;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.eventprototype.Db.DatabaseHandler;
import com.example.eventprototype.Model.EngagementModel;
import com.example.eventprototype.Model.EventModel;
import com.example.eventprototype.Model.UserModel;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SignUpExtraActivity extends AppCompatActivity {

    private final Calendar myCalendar = Calendar.getInstance();
    private AppCompatButton signUpBtn, loginSignupBtn;
    private TextInputEditText signUpFirstNameEt, signUpLastNameEt, signUpDegreeEt, signUpDobEt;
    private DatabaseHandler db;
    private List<UserModel> newUserInfo;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_extra);

        signUpBtn = findViewById(R.id.signUpBtn);
        loginSignupBtn = findViewById(R.id.loginSignupBtn);
        signUpFirstNameEt = findViewById(R.id.signUpFirstNameEt);
        signUpLastNameEt = findViewById(R.id.signUpLastNameEt);
        signUpDegreeEt = findViewById(R.id.signUpDegreeEt);
        signUpDobEt = findViewById(R.id.signUpDobEt);

        newUserInfo = (List<UserModel>) getIntent().getSerializableExtra("currentUserInfo");

        db = new DatabaseHandler(this);
        db.openDatabase();

        loginSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                startActivity(new Intent(SignUpExtraActivity.this, LoginActivity.class));
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    createNewUser();
                    createEngagements();
                    Toast.makeText(SignUpExtraActivity.this, "New User Created", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpExtraActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                catch (Exception e) {
                    Toast.makeText(SignUpExtraActivity.this, "Error with registration", Toast.LENGTH_SHORT).show();
                    System.out.println(e.getMessage());
                }

            }
        });

        signUpDobEt.setOnClickListener(new View.OnClickListener() {
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(SignUpExtraActivity.this,R.style.DatePickerCustom, date, myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
                datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.themeSecondaryColor));
                datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.themeSecondaryColor));

                //new DatePickerDialog(getContext(),R.style.DatePickerCustom ,date, myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        signUpFirstNameEt.addTextChangedListener(new TextWatcher() {
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

        signUpLastNameEt.addTextChangedListener(new TextWatcher() {
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

        signUpDegreeEt.addTextChangedListener(new TextWatcher() {
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

        signUpDobEt.addTextChangedListener(new TextWatcher() {
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
    }

    public void checkRequiredFields() {
        if (!signUpFirstNameEt.getText().toString().isEmpty() && !signUpLastNameEt.getText().toString().isEmpty()
                && !signUpDobEt.getText().toString().isEmpty() && !signUpDegreeEt.getText().toString().isEmpty()) {
            //Todo: add in checks for password length and checks for zId length and integrity
            signUpBtn.setEnabled(true);
        }
        else {
            signUpBtn.setEnabled(false);
        }
    }

    public void createNewUser() {
        //get the user signup information from the EditText fields
        String firstName = signUpFirstNameEt.getText().toString();
        String lastName = signUpLastNameEt.getText().toString();
        String degree = signUpDegreeEt.getText().toString();
        String oldDob = signUpDobEt.getText().toString();
        String newDob = null;
        try {
            newDob = convertDateFormat(oldDob);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        UserModel newUser = newUserInfo.get(0);
        //setting the setters of the User model
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setDegree(degree);
        newUser.setDob(newDob);
        db.insertUser(newUser);
    }

    public void createEngagements() {
        //get listing of all users
        List<UserModel> userList = db.getAllUsers();
        //targets the recently added user to the userList
        int recentUser = userList.size() - 1;
        EngagementModel engagementModel = new EngagementModel();
        //get list of all events
        List<EventModel> eventList = db.getAllEvents();
        //loop over all the events, creating a new engagement for each event in the system
        for (EventModel i : eventList) {
            engagementModel.setEventId(i.getId());
            engagementModel.setUserId(userList.get(recentUser).getId());
            //default is not joined
            engagementModel.setIsJoin(0);
            db.insertEngagement(engagementModel);
        }

    }

    public void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        signUpDobEt.setText(dateFormat.format(myCalendar.getTime()));
    }

    public String convertDateFormat(String oldDate) throws ParseException {
        SimpleDateFormat input = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat output = new SimpleDateFormat("yyyy/MM/dd");
        Date converted = input.parse(oldDate);
        String newDate = output.format(converted);
        System.out.println(newDate);
        return newDate;
    }




}
