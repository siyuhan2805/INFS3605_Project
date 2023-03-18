package com.example.eventprototype;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.eventprototype.Db.DatabaseHandler;

import java.util.Random;

public class SignUpActivity extends AppCompatActivity {
    private Random rand = new Random();
    private int upperbound = 1000;
    private int int_random = rand.nextInt(upperbound);
    private AppCompatButton btnSignUp;
    private AppCompatButton btnLogin;
    private DatabaseHandler database = new DatabaseHandler(this);
    //TODO: fix later
    private boolean isStaff = false;
    private EditText signUpPasswordEt;
    private EditText signUpZidEt;
    private TextView menuTitle;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //set up the toolbar
        menuTitle = findViewById(R.id.toolbar_heading_only);
        menuTitle.setText("");

        btnSignUp = findViewById(R.id.signUpBtn);
        btnLogin = findViewById(R.id.signLoginBtn);
        signUpPasswordEt = findViewById(R.id.signUpPasswordEt);
        signUpZidEt = findViewById(R.id.signUpZidEt);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });

    }
    public void createNewUser() {
        try {
            String zId = signUpZidEt.getText().toString();
            String password = signUpPasswordEt.getText().toString();

            //TODO: fix up so it takes requirements into account
            // If the username and password are correct move to the home page
            // Otherwise display text "Incorrect username or password"
            if(true) {
                database.insertUsers(int_random, zId, password, 0);
                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
            } else {
                Toast.makeText(SignUpActivity.this,
                        "Error",
                        Toast.LENGTH_SHORT).show();
            }
        } catch(Exception e) {
            Toast.makeText(SignUpActivity.this, "Error"
                    + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
