package com.example.localloop.ui.category;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.localloop.DatabaseHelper;
import com.example.localloop.MainActivity;
import com.example.localloop.R;
import com.example.localloop.model.User;

public class SignupActivity extends AppCompatActivity {

    private EditText usernameInput, passwordInput, firstNameInput;
    private Spinner roleSpinner;
    private Button registerBtn;

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        db = new DatabaseHelper(this);

        usernameInput = findViewById(R.id.editTextUsername);
        passwordInput = findViewById(R.id.editTextPassword);
        firstNameInput = findViewById(R.id.editTextFirstName);
        roleSpinner = findViewById(R.id.spinnerRole);
        registerBtn = findViewById(R.id.buttonRegister);

        // Set up spinner with role options
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.roles_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);

        registerBtn.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String username = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String firstName = firstNameInput.getText().toString().trim();

        Object selectedRole = roleSpinner.getSelectedItem();
        if (selectedRole == null) {
            Toast.makeText(this, "Please select a role.", Toast.LENGTH_SHORT).show();
            return;
        }
        String role = selectedRole.toString();

        if (username.isEmpty() || password.isEmpty() || firstName.isEmpty()) {
            Toast.makeText(this, "Fill all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = new User();
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setRole(role);

        long result = db.insertUser(user, password);
        if (result != -1) {
            Toast.makeText(this, "Account created.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Signup failed.", Toast.LENGTH_SHORT).show();
        }
    }
}
