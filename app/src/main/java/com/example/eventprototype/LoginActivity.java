package com.example.eventprototype;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class LoginActivity extends AppCompatActivity {
    private EditText loginPasswordEt;
    private EditText loginZidEt;
    private AppCompatButton loginBtn;

    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBtn = findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                Login();
            }
        });
    }

    public void Login() {
        try {
            database.insertUsersDefault();
            // For now, just switch screens to master view without verification
            boolean check = database.login(loginZidEt.getText(), loginPasswordEt.getText());

            // If the username and password are correct - move to the PetTable screen
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
