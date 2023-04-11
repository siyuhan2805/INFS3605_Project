package com.example.eventprototype;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.eventprototype.Db.DatabaseHandler;
import com.example.eventprototype.Model.EngagementModel;
import com.example.eventprototype.Model.EventModel;
import com.example.eventprototype.Model.UserModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SignUpActivity extends AppCompatActivity {
    private Random rand = new Random();
    private int upperbound = 1000;
    private int int_random = rand.nextInt(upperbound);
    private AppCompatButton btnNext;
    private AppCompatButton btnLogin;
    private DatabaseHandler db;
    private SignUpActivity activity;
    private TextInputEditText signUpPasswordEt;
    private TextInputEditText signUpZidEt, signUpCountryEt;
    private CheckBox staffCheckBox, internationalCheckBox;
    private ArrayList<UserModel> newUserInfo;
    private TextView menuTitle;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //set up the toolbar
        menuTitle = findViewById(R.id.toolbar_heading_only_heading);
        menuTitle.setText("");

        staffCheckBox = findViewById(R.id.staffCheckBox);
        btnNext = findViewById(R.id.nextBtn);
        btnLogin = findViewById(R.id.loginSignupBtn);
        signUpPasswordEt = findViewById(R.id.signUpPasswordEt);
        signUpZidEt = findViewById(R.id.signUpZidEt);
        signUpCountryEt = findViewById(R.id.signUpCountryEt);
        internationalCheckBox = findViewById(R.id.internationalCheckBox);


        //open database
        db = new DatabaseHandler(this);
        db.openDatabase();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    storeNewUser();
                    Toast.makeText(SignUpActivity.this, "New User Info stored", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this, SignUpExtraActivity.class);
                    intent.putExtra("currentUserInfo", newUserInfo);
                    startActivity(intent);
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

        signUpCountryEt.addTextChangedListener(new TextWatcher() {
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
            btnNext.setEnabled(true);
        }
        else {
            btnNext.setEnabled(false);
        }
    }

    //method to store the zId, password, isInternational and isStaff information to be passed onto the next activity
    public void storeNewUser() {
        //get the user signup information from the EditText fields
        String username = signUpZidEt.getText().toString();
        String password = signUpPasswordEt.getText().toString();
        String country = signUpCountryEt.getText().toString();
        int isStaff;
        int isInternational;
        //check if staff checkbox is checked or not
        if (staffCheckBox.isChecked()) {
            isStaff = 1;
        }
        else {
            isStaff = 0;
        }

        if (internationalCheckBox.isChecked()) {
            isInternational = 1;
        }
        else {
            isInternational = 0;
        }
        //initialise the User object
        UserModel user = new UserModel();
        //setting the setters of the User model
        user.setIsStaff(isStaff);
        user.setUsername(username);
        user.setPassword(password);
        user.setIsInternational(isInternational);
        user.setCountry(country);
        newUserInfo = new ArrayList<UserModel>();
        newUserInfo.add(user);
    }

    //method to ensure new users have the same engagements as existing users

}
