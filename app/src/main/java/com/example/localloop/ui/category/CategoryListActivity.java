package com.example.localloop.ui.category;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localloop.R;
import com.example.localloop.DatabaseHelper;
import com.example.localloop.DashboardActivity;

public class CategoryListActivity extends AppCompatActivity {

    private CategoryAdapter adapter;
    private DatabaseHelper db;
    private String username, role;

    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_category_list);

        username = getIntent().getStringExtra("username");
        role = getIntent().getStringExtra("role");

        db = new DatabaseHelper(this);

        adapter = new CategoryAdapter(
                id -> {
                    Intent in = new Intent(this, AddEditCategoryActivity.class);
                    in.putExtra(AddEditCategoryActivity.EXTRA_ID, id);
                    startActivity(in);
                },
                id -> {
                    db.deleteCategoryById(id);
                    Toast.makeText(this, "Category deleted", Toast.LENGTH_SHORT).show();
                    adapter.submitList(db.getAllCategories());
                }
        );

        RecyclerView rv = findViewById(R.id.recyclerCategories);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        Button add = findViewById(R.id.btnAdd);
        add.setOnClickListener(v ->
                startActivity(new Intent(this, AddEditCategoryActivity.class))
        );

        Button returnButton = findViewById(R.id.buttonReturn);
        returnButton.setOnClickListener(v -> {
            Intent intent = new Intent(CategoryListActivity.this, DashboardActivity.class);
            intent.putExtra("username", username);
            intent.putExtra("role", role);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.submitList(db.getAllCategories());
    }
}
