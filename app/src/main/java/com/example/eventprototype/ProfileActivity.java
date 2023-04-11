package com.example.eventprototype;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventprototype.Model.UserModel;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
    private TextView menuTitle;
    private ImageView backBtn;
    private ArrayList<UserModel> userList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        menuTitle = findViewById(R.id.toolbar_heading_only);
        menuTitle.setText("");

        //set up the toolbar
        menuTitle = findViewById(R.id.toolbar_heading_logout);
        menuTitle.setText("Dashboard");
        backBtn = findViewById(R.id.logoutIcon);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this,
                        LoginActivity.class);
                intent.putExtra("currentUser", userList);
                startActivity(intent);
            }
        });
    }
}
