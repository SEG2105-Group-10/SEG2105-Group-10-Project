package com.example.localloop.ui.category;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.localloop.DatabaseHelper;
import com.example.localloop.R;
import com.example.localloop.model.JoinRequest;
import java.util.List;

public class ManageJoinRequestsActivity extends AppCompatActivity {

    private LinearLayout requestContainer;
    private DatabaseHelper db;
    private String username, role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_join_requests);

        username = getIntent().getStringExtra("username");
        role = getIntent().getStringExtra("role");

        Log.d("DEBUG", "ManageJoinRequestsActivity -> username: " + username + ", role: " + role);

        db = new DatabaseHelper(this);
        requestContainer = findViewById(R.id.requestContainer);

        Button buttonReturn = findViewById(R.id.buttonReturn);
        buttonReturn.setOnClickListener(v -> {
            Intent intent = new Intent(ManageJoinRequestsActivity.this, com.example.localloop.DashboardActivity.class);
            intent.putExtra("username", username);
            intent.putExtra("role", "organizer");
            startActivity(intent);
            finish();
        });

        if (username == null || role == null) {
            Toast.makeText(this, "Missing user information. Please log in again.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if ("organizer".equalsIgnoreCase(role.trim())) {
            loadRequests();
        } else {
            Toast.makeText(this, "Access denied: only organizers can view requests.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadRequests() {
        requestContainer.removeAllViews();
        List<JoinRequest> requests = db.getJoinRequestsByOrganizer(username);

        for (JoinRequest request : requests) {
            View view = getLayoutInflater().inflate(R.layout.item_join_request, null);

            TextView info = view.findViewById(R.id.textRequestInfo);
            Button acceptBtn = view.findViewById(R.id.buttonAccept);
            Button rejectBtn = view.findViewById(R.id.buttonReject);

            info.setText("Event ID: " + request.getEventId() +
                    "\nUser: " + request.getAttendeeUsername() +
                    "\nStatus: " + request.getStatus());

            if (!"pending".equalsIgnoreCase(request.getStatus())) {
                acceptBtn.setEnabled(false);
                rejectBtn.setEnabled(false);
            }

            acceptBtn.setOnClickListener(v -> {
                db.updateRequestStatus(request.getId(), "accepted");
                loadRequests();
            });

            rejectBtn.setOnClickListener(v -> {
                db.updateRequestStatus(request.getId(), "rejected");
                loadRequests();
            });

            requestContainer.addView(view);
        }
    }
}
