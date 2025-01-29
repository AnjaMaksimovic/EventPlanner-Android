package com.example.eventplanner.activities.products;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eventplanner.R;
import com.example.eventplanner.adapters.SubcategoryNameAdapter;
import com.example.eventplanner.databinding.ActivityProductEditingBinding;
import com.example.eventplanner.model.Product;
import com.example.eventplanner.model.Subcategory;
import com.example.eventplanner.model.enums.EventType;
import com.example.eventplanner.repositories.ProductRepository;
import com.example.eventplanner.repositories.SubcategoryRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProductEditingActivity extends AppCompatActivity {

    private ActivityProductEditingBinding binding;
    private Product product;
    private Spinner subcategorySpinner;
    private EditText name;
    private EditText description;
    private EditText price;
    private EditText discount;
    private CheckBox availability;
    private CheckBox visibility;
    private List<EventType> checkedEventTypes = new ArrayList<>();
    private ProductRepository productRepo;
    private Subcategory selectedSubcategory;

    private SubcategoryRepository subcategoryRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProductEditingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        subcategoryRepo = new SubcategoryRepository();
        productRepo = new ProductRepository();
        subcategorySpinner = binding.productEditFormInclude.btnSubcategory;


        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("product")){
            product = intent.getParcelableExtra("product");
            Log.i("ShopApp", "Received Product: " + product.toString());

            EditText categoryEditText = binding.productEditFormInclude.categoryEditText;
            categoryEditText.setText(product.getCategory().getName()); // Assuming getCategory() returns category text



            name = binding.productEditFormInclude.nameField;
            name.setText(product.getName());

            description = binding.productEditFormInclude.descriptionField;
            description.setText(product.getDescription());

            price = binding.productEditFormInclude.priceField;
            price.setText(String.valueOf(product.getPrice())); // Assuming getPrice() returns double or int

            discount = binding.productEditFormInclude.discountField;
            discount.setText(String.valueOf(product.getDiscount()));

            setCheckBoxChecked();

            availability = findViewById(R.id.availability_checkbox);
            if(product.isAvailability()){
                availability.setChecked(true);
            } else {
                availability.setChecked(false);
            }

            visibility = findViewById(R.id.visibility_checkbox);
            if(product.isVisibility()){
                visibility.setChecked(true);
            } else {
                visibility.setChecked(false);
            }
        }

        setNewTypeEvents();

        ArrayList<Subcategory> subcategories = new ArrayList<>();
        Subcategory initialSubcategory = product.getSubcategory();

        subcategoryRepo.getSubcategoriesByCategoryId(product.getCategory().getId(), new SubcategoryRepository.SubcategoryFetchCallback() {
            @Override
            public void onSubcategoryFetch(ArrayList<Subcategory> fetchedSubcategories) {
                if (fetchedSubcategories != null && !fetchedSubcategories.isEmpty()){

                    subcategories.addAll(fetchedSubcategories);

                    SubcategoryNameAdapter subcategoryAdapter = new SubcategoryNameAdapter(ProductEditingActivity.this, android.R.layout.simple_spinner_item, fetchedSubcategories);
                    subcategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    subcategorySpinner.setAdapter(subcategoryAdapter);

                    subcategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            selectedSubcategory = subcategories.get(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                    if (initialSubcategory != null) {
                        int initialPosition = subcategoryAdapter.getPosition(initialSubcategory);
                        if (initialPosition != Spinner.INVALID_POSITION) {
                            subcategorySpinner.setSelection(initialPosition);
                        }
                    }
                } else {
                }
            }
        });

        Button submitBtn = binding.productEditFormInclude.submitBtn;
        submitBtn.setOnClickListener(v -> {
            editProduct();
            productRepo.updateProduct(product);
            finish();
        });

        ImageButton backBtn = binding.productEditFormInclude.btnBack;
        backBtn.setOnClickListener(v -> {
            finish();
        });
    }

    private void setCheckBoxChecked() {
        CheckBox privateEventsCheckBox = findViewById(R.id.private_events_checkbox);
        if (product.getTypes().contains(EventType.private_events)) {
            privateEventsCheckBox.setChecked(true);
        }

        CheckBox corporateEventsCheckBox = findViewById(R.id.corporate_events_checkbox);
        if (product.getTypes().contains(EventType.corporate)) {
            corporateEventsCheckBox.setChecked(true);
        }

        CheckBox culturalEventsCheckBox = findViewById(R.id.cultural_events_checkbox);
        if (product.getTypes().contains(EventType.cultural)) {
            culturalEventsCheckBox.setChecked(true);
        }

        CheckBox charityEventsCheckBox = findViewById(R.id.charity_events_checkbox);
        if (product.getTypes().contains(EventType.charity)) {
            charityEventsCheckBox.setChecked(true);
        }

        CheckBox politicalEventsCheckBox = findViewById(R.id.political_events_checkbox);
        if (product.getTypes().contains(EventType.political)) {
            politicalEventsCheckBox.setChecked(true);
        }

        CheckBox sportingEventsCheckBox = findViewById(R.id.sporting_events_checkbox);
        if (product.getTypes().contains(EventType.sporting)) {
            sportingEventsCheckBox.setChecked(true);
        }

        CheckBox educationalEventsCheckBox = findViewById(R.id.educational_events_checkbox);
        if (product.getTypes().contains(EventType.educational)) {
            educationalEventsCheckBox.setChecked(true);
        }
    }

    private void setNewTypeEvents(){
        CheckBox privateEventsCheckBox = binding.productEditFormInclude.privateEventsCheckbox;
        CheckBox corporateEventsCheckBox = binding.productEditFormInclude.corporateEventsCheckbox;
        CheckBox culturalEventsCheckBox = binding.productEditFormInclude.culturalEventsCheckbox;
        CheckBox educationalEventsCheckBox = binding.productEditFormInclude.educationalEventsCheckbox;
        CheckBox sportingEventsCheckBox = binding.productEditFormInclude.sportingEventsCheckbox;
        CheckBox charityEventsCheckBox = binding.productEditFormInclude.charityEventsCheckbox;
        CheckBox politicalEventsCheckBox = binding.productEditFormInclude.politicalEventsCheckbox;

        privateEventsCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                checkedEventTypes.add(EventType.private_events);
            } else {
                checkedEventTypes.remove(EventType.private_events);
            }
        });

        corporateEventsCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                checkedEventTypes.add(EventType.corporate);
            } else {
                checkedEventTypes.remove(EventType.corporate);
            }
        });

        culturalEventsCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                checkedEventTypes.add(EventType.cultural);
            } else {
                checkedEventTypes.remove(EventType.cultural);
            }
        });

        educationalEventsCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                checkedEventTypes.add(EventType.educational);
            } else {
                checkedEventTypes.remove(EventType.educational);
            }
        });

        sportingEventsCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                checkedEventTypes.add(EventType.sporting);
            } else {
                checkedEventTypes.remove(EventType.sporting);
            }
        });

        charityEventsCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                checkedEventTypes.add(EventType.charity);
            } else {
                checkedEventTypes.remove(EventType.charity);
            }
        });

        politicalEventsCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                checkedEventTypes.add(EventType.political);
            } else {
                checkedEventTypes.remove(EventType.political);
            }
        });
    }

    private void editProduct() {
        String productName = name.getText().toString();
        String productDescription = description.getText().toString();
        Double productPrice = Double.parseDouble(price.getText().toString());
        Double productDiscount = Double.parseDouble(discount.getText().toString());
        Boolean visible = visibility.isChecked();
        Boolean available = availability.isChecked();

        product.setSubcategory(selectedSubcategory);
        product.setPrice(productPrice);
        product.setDiscount(productDiscount);
        product.setName(productName);
        product.setDescription(productDescription);
        product.setTypes(checkedEventTypes);
        product.setImages(new ArrayList<>());

        LocalDateTime currentTime = LocalDateTime.now();
        product.setLastChange(currentTime.toString());

        if(visible){
            product.setVisibility(true);
        } else {
            product.setVisibility(false);
        }

        if(available){
            product.setAvailability(true);
        }else {
            product.setAvailability(false);
        }

    }
}