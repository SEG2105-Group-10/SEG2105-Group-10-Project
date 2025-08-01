package com.example.localloop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.localloop.ui.category.CategoryListActivity;
import com.example.localloop.ui.category.ManageJoinRequestsActivity;
import com.example.localloop.ui.category.AddEventActivity;
import com.example.localloop.ui.category.SearchEventsActivity;
import com.example.localloop.ui.category.ManageUsersActivity; // <-- ADD THIS

public class DashboardActivity extends AppCompatActivity {

    private String username, role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        TextView welcomeMessage = findViewById(R.id.welcomeMessage);
        Button addEventBtn = findViewById(R.id.buttonAddEvent);
        Button viewRequestsBtn = findViewById(R.id.buttonViewJoinRequests);
        Button logoutBtn = findViewById(R.id.buttonLogout);
        Button categoryBtn = findViewById(R.id.categoryBtn);
        Button manageUsersBtn = findViewById(R.id.manageUsersBtn); // <-- ADD THIS

        // Get and normalize intent extras
        username = getIntent().getStringExtra("username");
        role = getIntent().getStringExtra("role");
        if (role != null) role = role.trim().toLowerCase();

        welcomeMessage.setText("Welcome, " + username + "!");

        // Hide all buttons initially
        addEventBtn.setVisibility(View.GONE);
        viewRequestsBtn.setVisibility(View.GONE);
        categoryBtn.setVisibility(View.GONE);
        manageUsersBtn.setVisibility(View.GONE); // <-- ADD THIS

        // Assign role-based button logic
        if ("participant".equals(role)) {
            addEventBtn.setVisibility(View.VISIBLE);
            addEventBtn.setText("My Events");
            addEventBtn.setOnClickListener(v -> {
                Intent intent = new Intent(this, SearchEventsActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("role", role);
                startActivity(intent);
            });
        } else if ("organizer".equals(role)) {
            addEventBtn.setVisibility(View.VISIBLE);
            viewRequestsBtn.setVisibility(View.VISIBLE);

            addEventBtn.setText("Create Event");
            addEventBtn.setOnClickListener(v -> {
                Intent intent = new Intent(this, AddEventActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            });

            viewRequestsBtn.setOnClickListener(v -> {
                Intent intent = new Intent(this, ManageJoinRequestsActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("role", role);
                startActivity(intent);
            });
        } else if ("admin".equals(role)) {
            // Do not show Add Event button for admins
            categoryBtn.setVisibility(View.VISIBLE);
            manageUsersBtn.setVisibility(View.VISIBLE); // <-- ADD THIS

            categoryBtn.setOnClickListener(v -> {
                Intent intent = new Intent(this, CategoryListActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("role", role);
                startActivity(intent);
            });

            manageUsersBtn.setOnClickListener(v -> {
                Intent intent = new Intent(DashboardActivity.this, ManageUsersActivity.class);
                intent.putExtra("username", getIntent().getStringExtra("username"));
                intent.putExtra("role", getIntent().getStringExtra("role"));
                startActivity(intent);
            });
        }

        logoutBtn.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
