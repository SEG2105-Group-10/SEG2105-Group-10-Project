package com.example.localloop.ui.category;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localloop.DatabaseHelper;
import com.example.localloop.DashboardActivity;
import com.example.localloop.MainActivity;
import com.example.localloop.R;
import com.example.localloop.model.Event;

import java.util.List;

public class ParticipantJoinEventsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EventJoinAdapter adapter;
    private List<Event> allEvents;
    private DatabaseHelper dbHelper;
    private String username, role, firstname;
    private Button buttonReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant_join_events);

        // Receive user session info
        username = getIntent().getStringExtra("username");
        role = getIntent().getStringExtra("role");
        firstname = getIntent().getStringExtra("firstname");

        // Guard against null session info
        if (username == null || role == null) {
            Toast.makeText(this, "Session expired. Please log in again.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return;
        }

        dbHelper = new DatabaseHelper(this);
        allEvents = dbHelper.getAllEvents();

        recyclerView = findViewById(R.id.recyclerViewJoinEvents);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new EventJoinAdapter(allEvents, event -> {
            Toast.makeText(this, "Requested to join: " + event.getName(), Toast.LENGTH_SHORT).show();
            // TODO: Implement real join logic
        });

        recyclerView.setAdapter(adapter);

        // Return to dashboard
        buttonReturn = findViewById(R.id.buttonReturnToDashboard);
        buttonReturn.setOnClickListener(v -> {
            Intent intent = new Intent(this, DashboardActivity.class);
            intent.putExtra("username", username);
            intent.putExtra("firstname", firstname);
            intent.putExtra("role", role);
            startActivity(intent);
            finish();
        });
    }
}
