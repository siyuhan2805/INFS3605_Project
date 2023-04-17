package com.example.eventprototype;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.eventprototype.Db.DatabaseHandler;
import com.example.eventprototype.Model.UserModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText loginPasswordEt;
    private TextInputEditText loginZidEt;
    private AppCompatButton loginBtn;
    private AppCompatButton signUpBtn;
    private ArrayList<UserModel> userList;
    private ArrayList<UserModel> currentUser;
    private DatabaseHandler db;
    private LoginActivity activity;
    private boolean isStaff = false;
    private TextView menuTitle;


    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //set up the toolbar
        menuTitle = findViewById(R.id.toolbar_heading_only_heading);
        menuTitle.setText("");

        loginBtn = findViewById(R.id.loginBtn);
        signUpBtn = findViewById(R.id.loginSignupBtn);
        loginPasswordEt = findViewById(R.id.loginPasswordEt);
        loginZidEt = findViewById(R.id.loginZidEt);

        //open database
        db = new DatabaseHandler(this);
        db.openDatabase();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //userList = (ArrayList<UserModel>) db.getUser("z5260197", "123456");
                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class );
                userList = (ArrayList<UserModel>) db.getUser("z123456", "123456");
                intent.putExtra("currentUser", userList);
                startActivity(intent);
            }
        });


/*
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                String inputUsername = loginZidEt.getText().toString();
                String inputPassword = loginPasswordEt.getText().toString();
                //calls db method to get all users matching the username and password inputted by the user
                userList = (ArrayList<UserModel>) db.getUser(inputUsername, inputPassword);
                //login works by checking whether the username and password entered by the user returns a User object
                if (userList.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Username & password incorrect", Toast.LENGTH_SHORT).show();
                }
                else {
                    System.out.println(userList.get(0).getUsername());
                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class );
                    intent.putExtra("currentUser", userList);
                    startActivity(intent);
                }
            }
        });

 */

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

        //methods to check for user entry into the Username and Password fields
        loginZidEt.addTextChangedListener(new TextWatcher() {
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

        loginPasswordEt.addTextChangedListener(new TextWatcher() {
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
        if (!loginPasswordEt.getText().toString().isEmpty() && !loginZidEt.getText().toString().isEmpty()) {
            //Todo: add in checks for password length and checks for zId length and integrity
            loginBtn.setEnabled(true);
        }
        else {
            loginBtn.setEnabled(false);
        }
    }


}
