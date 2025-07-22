package com.example.localloop.ui.category;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.localloop.R;
import com.example.localloop.DatabaseHelper;
import com.example.localloop.model.Category;

public class AddEditCategoryActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "category_id";

    private EditText nameIn, descIn;
    private DatabaseHelper db;
    private int id = -1;

    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_add_edit_category);

        nameIn = findViewById(R.id.editName);
        descIn = findViewById(R.id.editDesc);
        Button save = findViewById(R.id.btnSave);
        db = new DatabaseHelper(this);

        if (getIntent().hasExtra(EXTRA_ID)) {
            id = getIntent().getIntExtra(EXTRA_ID, -1);
            Category c = db.getCategoryById(id);
            if (c != null) {
                nameIn.setText(c.getName());
                descIn.setText(c.getDescription());
            }
        }

        save.setOnClickListener(v -> commit());
    }

    private void commit() {
        String n = nameIn.getText().toString().trim();
        String d = descIn.getText().toString().trim();

        if (n.isEmpty() || d.isEmpty()) {
            Toast.makeText(this, "Fields must not be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (db.categoryNameExists(n, id)) {
            Toast.makeText(this, "Duplicate name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (id == -1) {
            db.insertCategory(new Category(n, d));
        } else {
            db.updateCategory(new Category(id, n, d));
        }

        finish();
    }
}
