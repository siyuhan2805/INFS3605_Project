package com.example.eventprototype;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.eventprototype.Model.UserModel;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    private Button btnProfileUpdate;
    private TextView menuTitle;
    private ImageView backBtn;
    private ArrayList<UserModel> userList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page1);

        //set up the toolbar
        menuTitle = findViewById(R.id.toolbar_heading_backspace);
        menuTitle.setText("Profile");
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, DashboardActivity.class);
                intent.putExtra("currentUser", userList);
                startActivity(intent);
            }
        });

        btnProfileUpdate = findViewById(R.id.profile_update_button);


        btnProfileUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            }
        });

    }


}
