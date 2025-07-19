package com.example.localloop.ui.category;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.localloop.DatabaseHelper;
import com.example.localloop.R;
import com.example.localloop.model.Category;
import com.example.localloop.model.Event;
import com.example.localloop.DashboardActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddEventActivity extends AppCompatActivity {

    private EditText editTextName, editTextDescription, editTextFee;
    private Spinner spinnerCategory;
    private Button buttonPickDate, buttonPickTime, buttonSubmit, buttonReturn;
    private TextView textViewDateTime;

    private DatabaseHelper dbHelper;
    private String selectedDate = "", selectedTime = "";
    private List<Category> categoryList;
    private String username, role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        // Get role and username
        username = getIntent().getStringExtra("username");
        role = getIntent().getStringExtra("role");

        // Bind views
        editTextName = findViewById(R.id.editTextName);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextFee = findViewById(R.id.editTextFee);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        buttonPickDate = findViewById(R.id.buttonPickDate);
        buttonPickTime = findViewById(R.id.buttonPickTime);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        textViewDateTime = findViewById(R.id.textViewDateTime);
        buttonReturn = findViewById(R.id.buttonReturn);

        // DB helper
        dbHelper = new DatabaseHelper(this);

        // Load categories into Spinner
        loadCategories();

        // Pickers
        buttonPickDate.setOnClickListener(v -> pickDate());
        buttonPickTime.setOnClickListener(v -> pickTime());

        // Submit
        buttonSubmit.setOnClickListener(v -> submitEvent());

        // Return button
        buttonReturn.setOnClickListener(v -> {
            Intent intent = new Intent(AddEventActivity.this, DashboardActivity.class);
            intent.putExtra("username", username);
            intent.putExtra("role", role);
            startActivity(intent);
            finish();
        });
    }

    private void loadCategories() {
        categoryList = dbHelper.getAllCategories();
        List<String> names = new ArrayList<>();
        for (Category c : categoryList) {
            names.add(c.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
    }

    private void pickDate() {
        Calendar c = Calendar.getInstance();
        DatePickerDialog dpd = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
            updateDateTimeText();
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dpd.show();
    }

    private void pickTime() {
        Calendar c = Calendar.getInstance();
        TimePickerDialog tpd = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            selectedTime = String.format("%02d:%02d", hourOfDay, minute);
            updateDateTimeText();
        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);
        tpd.show();
    }

    private void updateDateTimeText() {
        textViewDateTime.setText("Date: " + selectedDate + " Time: " + selectedTime);
    }

    private void submitEvent() {
        String name = editTextName.getText().toString().trim();
        String desc = editTextDescription.getText().toString().trim();
        String feeStr = editTextFee.getText().toString().trim();

        // Validate name
        if (name.isEmpty()) {
            editTextName.setError("Name required");
            return;
        }

        // Validate description
        if (desc.length() < 5) {
            editTextDescription.setError("Description too short");
            return;
        }

        // Validate fee
        double fee = 0;
        try {
            fee = Double.parseDouble(feeStr);
            if (fee < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            editTextFee.setError("Fee must be a number ≥ 0");
            return;
        }

        // Validate date and time
        if (selectedDate.isEmpty() || selectedTime.isEmpty()) {
            Toast.makeText(this, "Please pick a date and time", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate category
        if (categoryList == null || categoryList.isEmpty()) {
            Toast.makeText(this, "No categories available", Toast.LENGTH_SHORT).show();
            return;
        }
        int categoryPosition = spinnerCategory.getSelectedItemPosition();
        int categoryId = categoryList.get(categoryPosition).getId();

        // Create Event and insert
        Event event = new Event(0, name, desc, fee, categoryId, selectedDate, selectedTime);
        long id = dbHelper.insertEvent(event);
        if (id > 0) {
            Toast.makeText(this, "Event added!", Toast.LENGTH_SHORT).show();
            finish(); // Close activity
        } else {
            Toast.makeText(this, "Error adding event.", Toast.LENGTH_SHORT).show();
        }
    }
}
