package com.example.localloop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.localloop.ui.category.CategoryListActivity;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        String username = getIntent().getStringExtra("username");

        TextView welcomeText = findViewById(R.id.welcomeMessage);
        welcomeText.setText("Welcome Admin! You are logged in as \"" + username + "\".");
    }
    public void openCategories(View view) {
        Intent i = new Intent(this, CategoryListActivity.class);
        startActivity(i);
    }

    public void openMyEvents(View view) {
        Intent i = new Intent(this, com.example.localloop.ui.organizer.MyEventsActivity.class);
        startActivity(i);
    }
}
