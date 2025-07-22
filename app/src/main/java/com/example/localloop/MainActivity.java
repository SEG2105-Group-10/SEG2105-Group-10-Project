package com.example.localloop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.localloop.R;
import com.example.localloop.ui.category.SignupActivity;

import androidx.appcompat.app.AppCompatActivity;

import com.example.localloop.model.User;

public class MainActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonLogin;
    private Button buttonSignup;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonSignup = findViewById(R.id.buttonSignup);

        buttonLogin.setOnClickListener(v -> handleLogin());
        buttonSignup.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SignupActivity.class);
            startActivity(intent);
        });
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
            if (user.isDisabled()) {
                Toast.makeText(this, "Your account has been disabled by the admin.", Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                intent.putExtra("username", user.getUsername());
                intent.putExtra("firstname", user.getFirstName());
                intent.putExtra("role", user.getRole());
                startActivity(intent);
                finish();
            }
        } else {
            Toast.makeText(this, "Invalid login credentials.", Toast.LENGTH_SHORT).show();
        }
    }
}
