package com.example.eventprototype;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.eventprototype.Db.DatabaseHandler;

import java.util.Random;

public class LoginActivity extends AppCompatActivity {
    private EditText loginPasswordEt;
    private EditText loginZidEt;
    private AppCompatButton loginBtn;
    private AppCompatButton signUpBtn;
    private DatabaseHandler database = new DatabaseHandler(this);
    //TODO: fix later
    private boolean isStaff = false;

    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBtn = findViewById(R.id.loginBtn);
        signUpBtn = findViewById(R.id.loginSignupBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                Login();
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
    }

    public void Login() {
        try {
            String zId = loginZidEt.getText().toString();
            String password = loginPasswordEt.getText().toString();

            boolean check = database.login(zId, password, isStaff);

            // If the username and password are correct move to the home page
            // Otherwise display text "Incorrect username or password"
            if(check) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            } else {
                Toast.makeText(LoginActivity.this,
                        "Username and Password do not match",
                        Toast.LENGTH_SHORT).show();
            }
        } catch(Exception e) {
            Toast.makeText(LoginActivity.this, "Error"
                    + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
