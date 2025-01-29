package com.example.eventplanner.activities.categories;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
import com.example.eventplanner.adapters.CategoryNameAdapter;
import com.example.eventplanner.databinding.ActivityCreateSubcategoryBinding;
import com.example.eventplanner.model.Category;
import com.example.eventplanner.model.Subcategory;
import com.example.eventplanner.repositories.CategoryRepository;
import com.example.eventplanner.repositories.SubcategoryRepository;

import java.util.ArrayList;

public class CreateSubcategoryActivity extends AppCompatActivity {

    ActivityCreateSubcategoryBinding binding;
    private EditText name;
    private EditText description;
    private Spinner categorySpinner;
    private CheckBox service;
    private CheckBox product;
    private SubcategoryRepository subcategoryRepository;
    private CategoryRepository categoryRepository;
    private Category selectedCategory;
    private Subcategory newSubcategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateSubcategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /*Button btnSubmit = findViewById(R.id.submit_btn);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //FragmentTransition.to(EventType.newInstance(), EventTypeCreationActivity.this, false, R.id.downView);
            }
        });*/

        name = binding.categoryNameField;
        description = binding.categoryDescriptionField;
        service = binding.availabilityCheckbox;
        product = binding.visibilityCheckbox;
        categorySpinner = binding.spinCategory;


        categoryRepository = new CategoryRepository();
        subcategoryRepository = new SubcategoryRepository();

        ArrayList<Category> categories = new ArrayList<>();
        categoryRepository.getAllCategories(new CategoryRepository.CategoryFetchCallback(){
            @Override
            public void onCategoryFetch(ArrayList<Category> fetchedCategories) {
                if (fetchedCategories != null){
                    categories.addAll(fetchedCategories);
                    CategoryNameAdapter categoryAdapter = new CategoryNameAdapter(CreateSubcategoryActivity.this, android.R.layout.simple_spinner_item, categories);
                    categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    categorySpinner.setAdapter(categoryAdapter);

                    categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            selectedCategory = categories.get(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }
            }
        });

        Button submitBtn = binding.submitBtn;
        submitBtn.setOnClickListener(v -> {
            createNewProduct();

            subcategoryRepository.createSubcategory(newSubcategory);
            finish();
        });


    }

    private void createNewProduct() {
        String subcName = name.getText().toString();
        String subcDescription = description.getText().toString();
        Boolean serv = service.isChecked();
        Boolean prod = product.isChecked();

        newSubcategory = new Subcategory();
        newSubcategory.setCategoryId(selectedCategory.getId());
        newSubcategory.setName(subcName);
        newSubcategory.setDescription(subcDescription);
        if (serv) {
            newSubcategory.setType("Service");
        }else{
            newSubcategory.setType("Product");
        }

        if (prod) {
            newSubcategory.setType("Product");
        }else{
            newSubcategory.setType("Service");
        }

        // Provera da li je izabrana kategorija
        if (selectedCategory != null) {
            // Provera da li kategorija već ima listu potkategorija
            if (selectedCategory.getSubcategories() == null) {
                selectedCategory.setSubcategories(new ArrayList<>());
            }
            // Dodavanje nove potkategorije u listu potkategorija izabrane kategorije
            selectedCategory.getSubcategories().add(newSubcategory);

            // Ažuriranje kategorije u bazi podataka
            categoryRepository.updateCategory(selectedCategory);
        }
    }

}