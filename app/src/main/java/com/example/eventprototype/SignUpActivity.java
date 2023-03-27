package com.example.eventprototype;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.eventprototype.Db.DatabaseHandler;
import com.example.eventprototype.Model.EngagementModel;
import com.example.eventprototype.Model.EventModel;
import com.example.eventprototype.Model.UserModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.Random;

public class SignUpActivity extends AppCompatActivity {
    private Random rand = new Random();
    private int upperbound = 1000;
    private int int_random = rand.nextInt(upperbound);
    private AppCompatButton btnSignUp;
    private AppCompatButton btnLogin;
    private DatabaseHandler db;
    private SignUpActivity activity;
    private TextInputEditText signUpPasswordEt;
    private TextInputEditText signUpZidEt;
    private CheckBox staffCheckBox;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        staffCheckBox = findViewById(R.id.staffCheckBox);
        btnSignUp = findViewById(R.id.signUpBtn);
        btnLogin = findViewById(R.id.loginSignupBtn);
        signUpPasswordEt = findViewById(R.id.signUpPasswordEt);
        signUpZidEt = findViewById(R.id.signUpZidEt);

        //open database
        db = new DatabaseHandler(this);
        db.openDatabase();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    createNewUser();
                    createEngagements();
                    Toast.makeText(SignUpActivity.this, "New User Created", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignUpActivity.this, LoginActivity.class));

                }
                catch (Exception e) {
                    Toast.makeText(SignUpActivity.this, "Error with registration", Toast.LENGTH_SHORT).show();
                    System.out.println(e.getMessage());
                }

            }
        });

        signUpZidEt.addTextChangedListener(new TextWatcher() {
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

        signUpPasswordEt.addTextChangedListener(new TextWatcher() {
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


    //function to check whether the fields are zID are 8 characters long and start with a z
    public void checkRequiredFields() {
        if (!signUpPasswordEt.getText().toString().isEmpty() && !signUpZidEt.getText().toString().isEmpty()) {
            //Todo: add in checks for password length and checks for zId length and integrity
            btnSignUp.setEnabled(true);
        }
        else {
            btnSignUp.setEnabled(false);
        }
    }
    public void createNewUser() {
        //get the user signup information from the EditText fields
        String username = signUpZidEt.getText().toString();
        String password = signUpPasswordEt.getText().toString();
        int isStaff;
        //check if staff checkbox is checked or not
        if (staffCheckBox.isChecked()) {
            isStaff = 1;
        }
        else {
            isStaff = 0;
        }
        //initialise the User object
        UserModel user = new UserModel();
        //setting the setters of the User model
        user.setIsStaff(isStaff);
        user.setUsername(username);
        user.setPassword(password);
        db.insertUser(user);
    }

    //method to ensure new users have the same engagements as existing users
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
}
