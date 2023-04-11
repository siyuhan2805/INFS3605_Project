package com.example.eventprototype;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class StartActivity extends AppCompatActivity {
    private AppCompatButton btnSignUp;
    private AppCompatButton btnLogin;
    private TextView menuTitle;

    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //set up the toolbar
        menuTitle = findViewById(R.id.toolbar_heading_only_heading);
        menuTitle.setText("");

        btnSignUp = findViewById(R.id.startSignUpBtn);
        btnLogin = findViewById(R.id.startLoginBtn);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                startActivity(new Intent(StartActivity.this, LoginActivity.class));
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                startActivity(new Intent(StartActivity.this, SignUpActivity.class));
            }
        });
    }
}