package com.example.localloop.ui.category;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localloop.DatabaseHelper;
import com.example.localloop.R;
import com.example.localloop.model.Category;
import com.example.localloop.model.Event;
import com.example.localloop.DashboardActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchEventsActivity extends AppCompatActivity {

    private EditText editTextSearch;
    private Spinner spinnerCategory;
    private RecyclerView recyclerViewEvents;
    private Button buttonReturn;

    private EventAdapter eventAdapter;
    private List<Event> allEvents = new ArrayList<>();
    private List<Event> filteredEvents = new ArrayList<>();
    private List<String> categoryList = new ArrayList<>();
    private Map<Integer, String> categoryMap = new HashMap<>();

    private DatabaseHelper dbHelper;
    private String username, role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_events);

        username = getIntent().getStringExtra("username");
        role = getIntent().getStringExtra("role");

        editTextSearch = findViewById(R.id.editTextSearch);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        recyclerViewEvents = findViewById(R.id.recyclerViewEvents);
        buttonReturn = findViewById(R.id.buttonReturn);

        dbHelper = new DatabaseHelper(this);

        setupCategorySpinner();
        setupRecyclerView();
        setupListeners();

        loadAllEvents();

        buttonReturn.setOnClickListener(v -> {
            Intent intent = new Intent(SearchEventsActivity.this, DashboardActivity.class);
            intent.putExtra("username", username);
            intent.putExtra("role", role);
            startActivity(intent);
            finish();
        });
    }

    private void setupCategorySpinner() {
        List<Category> categories = dbHelper.getAllCategories();
        categoryList.clear();
        categoryMap.clear();

        categoryList.add("All");

        for (Category cat : categories) {
            categoryList.add(cat.getName());
            categoryMap.put(cat.getId(), cat.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, categoryList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
    }

    private void setupRecyclerView() {
        eventAdapter = new EventAdapter(filteredEvents, event -> {
            // You can define behavior on event click if needed
        });
        recyclerViewEvents.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewEvents.setAdapter(eventAdapter);
    }

    private void setupListeners() {
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterEvents();
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterEvents();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void loadAllEvents() {
        allEvents = dbHelper.getAllEvents(); // Already exists
        filterEvents();
    }

    private void filterEvents() {
        String searchQuery = editTextSearch.getText().toString().trim().toLowerCase();
        String selectedCategory = spinnerCategory.getSelectedItem().toString();

        filteredEvents.clear();

        for (Event event : allEvents) {
            String eventCategoryName = categoryMap.getOrDefault(event.getCategoryId(), "Unknown");

            boolean matchesName = event.getName().toLowerCase().contains(searchQuery);
            boolean matchesCategory = selectedCategory.equals("All") ||
                    eventCategoryName.equalsIgnoreCase(selectedCategory);

            if (matchesName && matchesCategory) {
                filteredEvents.add(event);
            }
        }

        eventAdapter.notifyDataSetChanged();
    }
}
