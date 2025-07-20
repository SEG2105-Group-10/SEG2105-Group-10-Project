package com.example.localloop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.localloop.ui.category.AddEventActivity;
import com.example.localloop.ui.category.CategoryListActivity;
import com.example.localloop.ui.category.SearchEventsActivity;
import com.example.localloop.ui.category.ParticipantJoinEventsActivity;

public class DashboardActivity extends AppCompatActivity {

    private String username, firstname, role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        username = getIntent().getStringExtra("username");
        firstname = getIntent().getStringExtra("firstname");
        role = getIntent().getStringExtra("role");

        TextView welcomeText = findViewById(R.id.welcomeMessage);
        if (firstname != null && role != null) {
            welcomeText.setText("Welcome " + firstname + "! You are logged in as \"" + role + "\".");
        }

        Button buttonAddEvent = findViewById(R.id.buttonAddEvent);
        Button buttonManageCategories = findViewById(R.id.buttonManageCategories);
        Button buttonSearchEvents = findViewById(R.id.buttonSearchEvents);
        Button buttonJoinEvents = findViewById(R.id.buttonJoinEvents);
        Button buttonLogout = findViewById(R.id.buttonLogout);

        boolean isAdmin = "admin".equalsIgnoreCase(role);
        boolean isParticipant = "participant".equalsIgnoreCase(role);

        buttonAddEvent.setVisibility(isAdmin ? View.VISIBLE : View.GONE);
        buttonManageCategories.setVisibility(isAdmin ? View.VISIBLE : View.GONE);
        buttonJoinEvents.setVisibility(isParticipant ? View.VISIBLE : View.GONE);

        buttonAddEvent.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEventActivity.class);
            intent.putExtra("username", username);
            intent.putExtra("role", role);
            startActivity(intent);
        });

        buttonManageCategories.setOnClickListener(v -> {
            Intent intent = new Intent(this, CategoryListActivity.class);
            intent.putExtra("username", username);
            intent.putExtra("role", role);
            startActivity(intent);
        });

        buttonSearchEvents.setOnClickListener(v -> {
            Intent intent = new Intent(this, SearchEventsActivity.class);
            intent.putExtra("username", username);
            intent.putExtra("role", role);
            startActivity(intent);
        });

        buttonJoinEvents.setOnClickListener(v -> {
            Intent intent = new Intent(this, ParticipantJoinEventsActivity.class);
            intent.putExtra("username", username);
            intent.putExtra("firstname", firstname);
            intent.putExtra("role", role);
            startActivity(intent);
        });

        buttonLogout.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}
