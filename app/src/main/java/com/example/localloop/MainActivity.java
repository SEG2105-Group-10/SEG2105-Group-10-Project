package com.example.localloop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.localloop.model.User;

public class MainActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonLogin;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Keep your original login layout

        dbHelper = new DatabaseHelper(this);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(v -> handleLogin());
    }

    private void handleLogin() {
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both username and password.", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = dbHelper.getUser(username, password);
        if (user != null) {
            Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
            intent.putExtra("username", user.getUsername());
            intent.putExtra("firstname", user.getFirstName());
            intent.putExtra("role", user.getRole());
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Invalid login credentials.", Toast.LENGTH_SHORT).show();
        }
    }
}
