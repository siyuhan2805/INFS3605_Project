package com.example.eventprototype;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventprototype.Model.UserModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ProfileActivity extends AppCompatActivity {
    private ArrayList<UserModel> currentUser;
    private TextView profile_name, profile_email, profile_text_DOB, profile_text_studentID, profile_text_degree;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page1);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        //set the icon as selected
        bottomNavigationView.setSelectedItemId(R.id.profile);

        //get the intent passed from either profile or dashboard
        currentUser = (ArrayList<UserModel>) getIntent().getSerializableExtra("currentUser");

        profile_name = findViewById(R.id.profile_name);
        profile_email = findViewById(R.id.profile_email);
        profile_text_DOB = findViewById(R.id.profile_text_DOB);
        profile_text_studentID = findViewById(R.id.profile_text_studentID);
        profile_text_degree = findViewById(R.id.profile_text_degree);

        String fullName = currentUser.get(0).getFirstName() + " " + currentUser.get(0).getLastName();
        profile_name.setText(fullName);
        String email = currentUser.get(0).getUsername() + "@ad.unsw.edu.au";
        profile_email.setText(email);
        String newDate = null;
        try {
             newDate = convertDateFormat(currentUser.get(0).getDob());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        profile_text_DOB.setText(newDate);
        profile_text_studentID.setText(currentUser.get(0).getUsername());
        profile_text_degree.setText(currentUser.get(0).getDegree());


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.dashboard:
                        Intent intent = new Intent(ProfileActivity.this, DashboardActivity.class );
                        intent.putExtra("currentUser", currentUser);
                        startActivity(intent);
                        return true;

                    case R.id.events:
                        intent = new Intent(ProfileActivity.this, MainActivity.class );
                        intent.putExtra("currentUser", currentUser);
                        startActivity(intent);
                        return true;
                }
                return false;
            }
        });
    }

    public String convertDateFormat(String oldDate) throws ParseException {
        SimpleDateFormat input = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");
        Date converted = input.parse(oldDate);
        String newDate = output.format(converted);
        System.out.println(newDate);
        return newDate;
    }
}
