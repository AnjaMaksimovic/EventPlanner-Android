package com.example.eventplanner.activities.categories;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventplanner.R;
import com.example.eventplanner.databinding.ActivityEditCategoryBinding;
import com.example.eventplanner.databinding.ActivityProductEditingBinding;
import com.example.eventplanner.model.Category;
import com.example.eventplanner.repositories.CategoryRepository;

public class EditCategoryActivity extends AppCompatActivity {

    private ActivityEditCategoryBinding binding;
    private Category category;
    private EditText name;
    private EditText description;
    private CategoryRepository categoryRepo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        //setContentView(R.layout.activity_edit_category);
        binding = ActivityEditCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        categoryRepo = new CategoryRepository();


        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("category")){
            category = intent.getParcelableExtra("category");
            Log.i("ShopApp", "Received Product: " + category.toString());

            name = binding.eventTypeNameField;
            name.setText(category.getName());

            description = binding.eventTypeDescriptionField;
            description.setText(category.getDescription());

        }

        Button submitBtn = binding.submitBtn;
        submitBtn.setOnClickListener(v -> {
            //editCategory();
            category.setName(binding.eventTypeNameField.getText().toString());
            category.setDescription(binding.eventTypeDescriptionField.getText().toString());
            categoryRepo.updateCategory(category);
            finish();
        });
    }
}