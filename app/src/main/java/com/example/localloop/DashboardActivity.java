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

public class DashboardActivity extends AppCompatActivity {

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        username = getIntent().getStringExtra("username");

        TextView welcomeText = findViewById(R.id.welcomeMessage);
        welcomeText.setText("Welcome, " + username + "!");

        Button buttonAddEvent = findViewById(R.id.buttonAddEvent);
        Button buttonManageCategories = findViewById(R.id.buttonManageCategories);
        Button buttonSearchEvents = findViewById(R.id.buttonSearchEvents);
        Button buttonLogout = findViewById(R.id.buttonLogout);

        boolean isAdmin = "admin".equalsIgnoreCase(username);

        buttonAddEvent.setVisibility(isAdmin ? View.VISIBLE : View.GONE);
        buttonManageCategories.setVisibility(isAdmin ? View.VISIBLE : View.GONE);

        buttonAddEvent.setOnClickListener(v -> startActivity(new Intent(this, AddEventActivity.class)));
        buttonManageCategories.setOnClickListener(v -> startActivity(new Intent(this, CategoryListActivity.class)));
        buttonSearchEvents.setOnClickListener(v -> startActivity(new Intent(this, SearchEventsActivity.class)));
        buttonLogout.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}
