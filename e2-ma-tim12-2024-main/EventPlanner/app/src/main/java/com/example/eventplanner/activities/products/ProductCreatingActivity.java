package com.example.eventplanner.activities.products;

import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventplanner.R;
import com.example.eventplanner.adapters.CategoryNameAdapter;
import com.example.eventplanner.adapters.SubcategoryNameAdapter;
import com.example.eventplanner.databinding.ActivityProductCreatingBinding;
import com.example.eventplanner.model.Category;
import com.example.eventplanner.model.Product;
import com.example.eventplanner.model.Subcategory;
import com.example.eventplanner.model.SubcategorySuggestion;
import com.example.eventplanner.model.enums.EventType;
import com.example.eventplanner.model.enums.ProductState;
import com.example.eventplanner.repositories.CategoryRepository;
import com.example.eventplanner.repositories.ProductRepository;
import com.example.eventplanner.repositories.SubcategoryRepository;
import com.example.eventplanner.repositories.SubcategorySuggestionRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProductCreatingActivity extends AppCompatActivity {
    Context context;
    private ActivityProductCreatingBinding binding;
    private Spinner categorySpinner;
    private Spinner subcategorySpinner;
    private EditText name;
    private EditText description;
    private EditText price;
    private EditText discount;
    private CheckBox availability;
    private CheckBox visibility;
    private List<EventType> checkedEventTypes = new ArrayList<>();

    private ProductRepository productRepo;

    private SubcategoryRepository subcategoryRepo;
    private CategoryRepository categoryRepo;
    private SubcategorySuggestionRepository suggestionRepo;
    private Subcategory selectedSubcategory;
    private Category selectedCategory;
    private Product newProduct;
    private boolean suggestedSubcategory;
    private Button suggestionBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProductCreatingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        suggestedSubcategory = false;
        suggestionBtn = binding.productFormInclude.subcategorySuggestBtn;
        suggestionBtn.setVisibility(View.INVISIBLE);

        binding.productFormInclude.subcategorySuggestBtn.setOnClickListener(v -> {
                showAddSubcategoryDialog();
        });

        ImageButton backBtn = binding.productFormInclude.btnBack;
        backBtn.setOnClickListener(v -> {
            finish();
        });


        //creating product

        name = binding.productFormInclude.nameField;
        description = binding.productFormInclude.descriptionField;
        price = binding.productFormInclude.priceField;
        discount = binding.productFormInclude.discountField;
        visibility = binding.productFormInclude.visibilityCheckbox;
        availability = binding.productFormInclude.availabilityCheckbox;
        categorySpinner = binding.productFormInclude.spinCategory;
        subcategorySpinner = binding.productFormInclude.spinSubcategory;

        categoryRepo = new CategoryRepository();
        subcategoryRepo = new SubcategoryRepository();
        productRepo = new ProductRepository();
        suggestionRepo = new SubcategorySuggestionRepository();

        ArrayList<Category> categories = new ArrayList<>();
        ArrayList<Subcategory> subcategories = new ArrayList<>();
        categoryRepo.getAllCategories(new CategoryRepository.CategoryFetchCallback(){
            @Override
            public void onCategoryFetch(ArrayList<Category> fetchedCategories) {
                if (fetchedCategories != null){
                    categories.addAll(fetchedCategories);
                    CategoryNameAdapter categoryAdapter = new CategoryNameAdapter(ProductCreatingActivity.this, android.R.layout.simple_spinner_item, categories);
                    categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    categorySpinner.setAdapter(categoryAdapter);

                    categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            selectedCategory = categories.get(position);
                            subcategoryRepo.getSubcategoriesByCategoryId(selectedCategory.getId(), new SubcategoryRepository.SubcategoryFetchCallback() {
                                @Override
                                public void onSubcategoryFetch(ArrayList<Subcategory> fetchedSubcategories) {
                                    if (fetchedSubcategories != null && !fetchedSubcategories.isEmpty()){

                                        subcategories.addAll(fetchedSubcategories);

                                        SubcategoryNameAdapter subcategoryAdapter = new SubcategoryNameAdapter(ProductCreatingActivity.this, android.R.layout.simple_spinner_item, fetchedSubcategories);
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
                                    } else {
                                        subcategories.clear(); // Clear the subcategories list

                                        // Set an empty adapter to clear the spinner
                                        SubcategoryNameAdapter emptyAdapter = new SubcategoryNameAdapter(ProductCreatingActivity.this, android.R.layout.simple_spinner_item, new ArrayList<>());
                                        emptyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        subcategorySpinner.setAdapter(emptyAdapter);

                                        suggestionBtn.setVisibility(View.VISIBLE);
                                    }
                                }
                            });
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }
            }
        });

        CheckBox privateEventsCheckBox = binding.productFormInclude.privateEventsCheckbox;
        CheckBox corporateEventsCheckBox = binding.productFormInclude.corporateEventsCheckbox;
        CheckBox culturalEventsCheckBox = binding.productFormInclude.culturalEventsCheckbox;
        CheckBox educationalEventsCheckBox = binding.productFormInclude.educationalEventsCheckbox;
        CheckBox sportingEventsCheckBox = binding.productFormInclude.sportingEventsCheckbox;
        CheckBox charityEventsCheckBox = binding.productFormInclude.charityEventsCheckbox;
        CheckBox politicalEventsCheckBox = binding.productFormInclude.politicalEventsCheckbox;

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


        Button submitBtn = binding.productFormInclude.submitBtn;
        submitBtn.setOnClickListener(v -> {
            createNewProduct();

            productRepo.createProduct(newProduct);
            finish();
        });

        binding.productFormInclude.fabAddPhoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");

            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(this, "No app can handle this action", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void createNewProduct() {
        String productName = name.getText().toString();
        String productDescription = description.getText().toString();
        Double productPrice = Double.parseDouble(price.getText().toString());
        Double productDiscount = Double.parseDouble(discount.getText().toString());
        Boolean visible = visibility.isChecked();
        Boolean available = availability.isChecked();

        newProduct = new Product();
        newProduct.setCategory(selectedCategory);
        newProduct.setSubcategory(selectedSubcategory);
        newProduct.setPrice(productPrice);
        newProduct.setDiscount(productDiscount);
        newProduct.setName(productName);
        newProduct.setDescription(productDescription);
        newProduct.setTypes(checkedEventTypes);
        newProduct.setImages(new ArrayList<>());

        LocalDateTime currentTime = LocalDateTime.now();
        newProduct.setLastChange(currentTime.toString());

        if(suggestedSubcategory){
            newProduct.setProductState(ProductState.pending);
        } else {
            newProduct.setProductState(ProductState.approved);
        }

        if(visible){
            newProduct.setVisibility(true);
        } else {
            newProduct.setVisibility(false);
        }

        if(available){
            newProduct.setAvailability(true);
        }else {
            newProduct.setAvailability(false);
        }

    }

    private void showAddSubcategoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_subcategory, null);

        final EditText etSubcategoryName = dialogView.findViewById(R.id.etSubcategoryName);
        final EditText etSubcategoryDescription = dialogView.findViewById(R.id.etSubcategoryDescription);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        Button btnOk = dialogView.findViewById(R.id.btnOk);

        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubcategorySuggestion subcategorySuggestion=new SubcategorySuggestion();
                Category suggestionCategory=selectedCategory;
                suggestedSubcategory = true;
                subcategorySuggestion.setCategoryId(suggestionCategory.getId());
                subcategorySuggestion.setName(etSubcategoryName.getText().toString());
                subcategorySuggestion.setDescription(etSubcategoryDescription.getText().toString());
                subcategorySuggestion.setSubcategoryType("product");
                suggestionRepo.createSuggestion(subcategorySuggestion);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}