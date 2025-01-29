package com.example.eventplanner.activities.categories;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventplanner.R;
import com.example.eventplanner.databinding.ActivityCreateCategoryBinding;
import com.example.eventplanner.databinding.ActivityEventTypeCreationBinding;
import com.example.eventplanner.model.Category;
import com.example.eventplanner.repositories.CategoryRepository;

public class CreateCategoryActivity extends AppCompatActivity {

    private ActivityCreateCategoryBinding binding;
    private EditText name;
    private EditText description;
    private CategoryRepository categoryRepo;
    private Category newCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        name = binding.categoryNameField;
        description = binding.categoryDescriptionField;
        categoryRepo = new CategoryRepository();
        Button btnSubmit = findViewById(R.id.submit_btn);
        btnSubmit.setOnClickListener(v-> {
            newCategory = new Category();
            newCategory.setName(name.getText().toString());
            newCategory.setDescription(description.getText().toString());
            categoryRepo.createCategory(newCategory);
            finish();
        });
    }
}