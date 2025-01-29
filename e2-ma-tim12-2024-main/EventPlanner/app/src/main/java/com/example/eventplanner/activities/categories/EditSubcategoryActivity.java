package com.example.eventplanner.activities.categories;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventplanner.R;
import com.example.eventplanner.databinding.ActivityCreateSubcategoryBinding;
import com.example.eventplanner.databinding.ActivityEditCategoryBinding;
import com.example.eventplanner.databinding.ActivityEditSubcategoryBinding;
import com.example.eventplanner.model.Category;
import com.example.eventplanner.model.Subcategory;
import com.example.eventplanner.repositories.CategoryRepository;
import com.example.eventplanner.repositories.SubcategoryRepository;

public class EditSubcategoryActivity extends AppCompatActivity {

    private ActivityEditSubcategoryBinding binding;
    private Subcategory subcategory;
    private EditText categoryName;
    private EditText name;
    private EditText description;
    private SubcategoryRepository subcategoryRepository;
    private Spinner categorySpinner;
    private Category selectedCategory;
    private String categoryId;
    private CheckBox service;
    private CheckBox product;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        //setContentView(R.layout.activity_edit_subcategory);

        binding = ActivityEditSubcategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        subcategoryRepository = new SubcategoryRepository();



        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("subcategory")){
            subcategory = intent.getParcelableExtra("subcategory");
            Log.i("ShopApp", "Received Subcategory: " + subcategory.toString());



            name = binding.categoryNameField;
            name.setText(subcategory.getName());

            description = binding.categoryDescriptionField;
            description.setText(subcategory.getDescription());

            product = binding.visibilityCheckbox;
            service = binding.availabilityCheckbox;

            // Učitaj naziv kategorije i postavi u tekstualno polje
            loadCategoryName(subcategory.getCategoryId());
            categoryName = binding.selectedCategoryNameField;
            categoryId = subcategory.getCategoryId();

        }

        Button submitBtn = binding.submitBtn;
        submitBtn.setOnClickListener(v -> {
            editSubcategory();
            //subcategory.setName(binding.categoryNameField.getText().toString());
            //subcategory.setDescription(binding.categoryDescriptionField.getText().toString());
            Log.d("Editovana sub", subcategory.toString());
            Log.d("Editovana sub ID", subcategory.getId().toString());
            subcategoryRepository.updateSubcategory(subcategory);
            finish();
        });
    }
    private void loadCategoryName(String categoryId) {
        // Poziv metode getCategoryById iz CategoryRepository
        CategoryRepository categoryRepository = new CategoryRepository();
        categoryRepository.getCategoryById(categoryId, new CategoryRepository.CategoryFetchCallback() {
            @Override
            public void onCategoryFetched(Category category, String errorMessage) {
                if (category != null) {
                    selectedCategory = category;
                    // Postavi naziv kategorije u tekstualno polje
                    categoryName.setText(category.getName());
                    // Onemogući uređivanje tekstualnog polja
                    categoryName.setEnabled(false);
                    categoryName.setFocusable(false);

                } else {
                    Log.e("EditSubcategoryActivity", "Error fetching category: " + errorMessage);
                }
            }
        });
    }


    private void editSubcategory(){
        String subcName = name.getText().toString();
        String subcDescription = description.getText().toString();
        Boolean serv = service.isChecked();
        Boolean prod = product.isChecked();

        // Ne stvarajte novi objekat, koristite postojeći
        // subcategory = new Subcategory();

        // Postavite atribute postojećeg objekta
        subcategory.setName(subcName);
        subcategory.setDescription(subcDescription);
        subcategory.setCategoryId(categoryId);
        if (serv) {
            subcategory.setType("Service");
        } else {
            subcategory.setType("Product");
        }

        if (prod) {
            subcategory.setType("Product");
        } else {
            subcategory.setType("Service");
        }
    }

}