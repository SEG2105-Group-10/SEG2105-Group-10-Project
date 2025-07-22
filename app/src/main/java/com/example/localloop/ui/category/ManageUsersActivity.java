package com.example.localloop.ui.category;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localloop.DatabaseHelper;
import com.example.localloop.DashboardActivity;
import com.example.localloop.R;
import com.example.localloop.model.User;

import java.util.List;

public class ManageUsersActivity extends AppCompatActivity implements UserAdapter.OnUserActionListener {

    private DatabaseHelper db;
    private RecyclerView recyclerView;
    private UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);

        db = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerViewUsers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<User> users = db.getAllNonAdminUsers();
        adapter = new UserAdapter(users, this);
        recyclerView.setAdapter(adapter);

        Button returnButton = findViewById(R.id.buttonReturnDashboard);
        returnButton.setOnClickListener(v -> {
            Intent intent = new Intent(ManageUsersActivity.this, DashboardActivity.class);
            intent.putExtra("username", getIntent().getStringExtra("username"));
            intent.putExtra("role", getIntent().getStringExtra("role"));
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onDeleteClicked(User user) {
        db.deleteUserByUsername(user.getUsername());
        adapter.removeUser(user);
        Toast.makeText(this, "User deleted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onToggleStatusClicked(User user) {
        boolean newStatus = !user.isDisabled();
        db.setUserDisabled(user.getUsername(), newStatus);
        user.setDisabled(newStatus);
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "User " + (newStatus ? "disabled" : "enabled"), Toast.LENGTH_SHORT).show();
    }
}
