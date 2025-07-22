package com.example.localloop.ui.category;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.localloop.R;
import com.example.localloop.DatabaseHelper;
import com.example.localloop.model.Category;
import com.example.localloop.model.Event;

import java.util.Calendar;
import java.util.List;

public class AddEventActivity extends AppCompatActivity {

    private EditText editTextName, editTextDescription, editTextFee;
    private Spinner spinnerCategory;
    private Button buttonPickDate, buttonPickTime, buttonSubmit, buttonReturn;
    private TextView textViewDateTime;

    private String selectedDate = "", selectedTime = "", organizerUsername = "";
    private List<Category> categoryList;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        db = new DatabaseHelper(this);
        editTextName = findViewById(R.id.editTextName);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextFee = findViewById(R.id.editTextFee);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        buttonPickDate = findViewById(R.id.buttonPickDate);
        buttonPickTime = findViewById(R.id.buttonPickTime);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonReturn = findViewById(R.id.buttonReturn);
        textViewDateTime = findViewById(R.id.textViewDateTime);

        // Receive username from intent
        organizerUsername = getIntent().getStringExtra("username");

        loadCategories();

        buttonPickDate.setOnClickListener(v -> pickDate());
        buttonPickTime.setOnClickListener(v -> pickTime());

        buttonSubmit.setOnClickListener(v -> {
            String name = editTextName.getText().toString().trim();
            String description = editTextDescription.getText().toString().trim();
            String feeStr = editTextFee.getText().toString().trim();
            if (name.isEmpty() || description.isEmpty() || feeStr.isEmpty() || selectedDate.isEmpty() || selectedTime.isEmpty()) {
                Toast.makeText(this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            double fee;
            try {
                fee = Double.parseDouble(feeStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid fee format.", Toast.LENGTH_SHORT).show();
                return;
            }

            Category selectedCategory = categoryList.get(spinnerCategory.getSelectedItemPosition());

            Event event = new Event();
            event.setName(name);
            event.setDescription(description);
            event.setFee(fee);
            event.setCategoryId(selectedCategory.getId());
            event.setDate(selectedDate);
            event.setTime(selectedTime);
            event.setOrganizerUsername(organizerUsername); // SET organizer!

            db.addEvent(event);
            Toast.makeText(this, "Event added successfully.", Toast.LENGTH_SHORT).show();
        });

        buttonReturn.setOnClickListener(v -> {
            finish(); // Return to dashboard
        });
    }

    private void loadCategories() {
        categoryList = db.getAllCategories();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                categoryList.stream().map(Category::getName).toArray(String[]::new));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
    }

    private void pickDate() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
            updateDateTimeDisplay();
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void pickTime() {
        Calendar calendar = Calendar.getInstance();
        new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            selectedTime = String.format("%02d:%02d", hourOfDay, minute);
            updateDateTimeDisplay();
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }

    private void updateDateTimeDisplay() {
        if (!selectedDate.isEmpty() && !selectedTime.isEmpty()) {
            textViewDateTime.setText(selectedDate + " " + selectedTime);
        }
    }
}
