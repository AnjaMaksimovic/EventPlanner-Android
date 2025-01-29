package com.example.eventplanner.activities.services;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventplanner.R;
import com.example.eventplanner.adapters.CategoryNameAdapter;
import com.example.eventplanner.adapters.SubcategoryNameAdapter;
import com.example.eventplanner.databinding.ActivityServiceCreatingBinding;
import com.example.eventplanner.model.Category;
import com.example.eventplanner.model.Person;
import com.example.eventplanner.model.Service;
import com.example.eventplanner.model.Subcategory;
import com.example.eventplanner.model.SubcategorySuggestion;
import com.example.eventplanner.model.enums.EventType;
import com.example.eventplanner.model.enums.ProductState;
import com.example.eventplanner.model.enums.ReservationMethod;
import com.example.eventplanner.repositories.CategoryRepository;
import com.example.eventplanner.repositories.PersonRepository;
import com.example.eventplanner.repositories.ServiceRepository;
import com.example.eventplanner.repositories.SubcategoryRepository;
import com.example.eventplanner.repositories.SubcategorySuggestionRepository;
import com.example.eventplanner.repositories.interfaces.IPersonRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServiceCreatingActivity extends AppCompatActivity {

    private ActivityServiceCreatingBinding binding;
    private Spinner categorySpinner;
    private Spinner subcategorySpinner;
    private EditText name;
    private EditText description;
    private EditText specifics;
    private EditText price;
    private EditText duration;
    private EditText reservationDeadline;
    private EditText cancelationDeadline;
    private EditText discount;
    private CheckBox availability;
    private CheckBox visibility;
    private List<EventType> checkedEventTypes = new ArrayList<>();

    private ServiceRepository serviceRepo;
    private SubcategoryRepository subcategoryRepo;
    private CategoryRepository categoryRepo;
    private PersonRepository personRepo;
    private SubcategorySuggestionRepository suggestionRepo;
    private Subcategory selectedSubcategory;
    private Category selectedCategory;
    private List<Person> selectedEmployees = new ArrayList<>();
    private Service newService;
    private boolean suggestedSubcategory;
    private Button suggestionBtn;

    private RadioButton radioButtonManually;
    private RadioButton radioButtonAutomatic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityServiceCreatingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        suggestedSubcategory = false;
        suggestionBtn = binding.serviceFormInclude.subcategorySuggestBtn;
        suggestionBtn.setVisibility(View.INVISIBLE);


        binding.serviceFormInclude.subcategorySuggestBtn.setOnClickListener(v -> {
            showAddSubcategoryDialog();
        });

        ImageButton backBtn = binding.serviceFormInclude.btnBack;
        backBtn.setOnClickListener(v -> {
            finish();
        });


        name = binding.serviceFormInclude.nameField;
        specifics = binding.serviceFormInclude.specificsField;
        duration = binding.serviceFormInclude.durationField;
        reservationDeadline = binding.serviceFormInclude.reservationDeadlineField;
        cancelationDeadline = binding.serviceFormInclude.cancelationDeadlineField;
        description = binding.serviceFormInclude.descriptionField;
        price = binding.serviceFormInclude.priceField;
        discount = binding.serviceFormInclude.discountField;
        visibility = binding.serviceFormInclude.visibilityCheckbox;
        availability = binding.serviceFormInclude.availabilityCheckbox;
        categorySpinner = binding.serviceFormInclude.spinCategory;
        subcategorySpinner = binding.serviceFormInclude.spinSubcategory;
        radioButtonAutomatic = binding.serviceFormInclude.radioButtonAutomatic;
        radioButtonManually = binding.serviceFormInclude.radioButtonManual;

        categoryRepo = new CategoryRepository();
        subcategoryRepo = new SubcategoryRepository();
        serviceRepo = new ServiceRepository();
        suggestionRepo = new SubcategorySuggestionRepository();
        personRepo = new PersonRepository();

        ArrayList<Category> categories = new ArrayList<>();
        ArrayList<Subcategory> subcategories = new ArrayList<>();

        categoryRepo.getAllCategories(new CategoryRepository.CategoryFetchCallback() {
            @Override
            public void onCategoryFetch(ArrayList<Category> fetchedCategories) {
                if (fetchedCategories != null){
                    categories.addAll(fetchedCategories);
                    CategoryNameAdapter categoryAdapter = new CategoryNameAdapter(ServiceCreatingActivity.this, android.R.layout.simple_spinner_item, categories);
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

                                        SubcategoryNameAdapter subcategoryAdapter = new SubcategoryNameAdapter(ServiceCreatingActivity.this, android.R.layout.simple_spinner_item, fetchedSubcategories);
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
                                        SubcategoryNameAdapter emptyAdapter = new SubcategoryNameAdapter(ServiceCreatingActivity.this, android.R.layout.simple_spinner_item, new ArrayList<>());
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

        ArrayList<Person> employees = new ArrayList<>();

        personRepo.GetAllByCompany("Company_2", new IPersonRepository.GetPersonsCallback() {
            @Override
            public void OnGetPeople(ArrayList<Person> persons) {
                employees.addAll(persons);
            }
        });

        Button selectEmployeesButton = binding.serviceFormInclude.employeesSelectBtn;
        selectEmployeesButton.setOnClickListener(v -> {
            showEmployeesPopup(v, employees);
        });


        CheckBox privateEventsCheckBox = binding.serviceFormInclude.privateEventsCheckbox;
        CheckBox corporateEventsCheckBox = binding.serviceFormInclude.corporateEventsCheckbox;
        CheckBox culturalEventsCheckBox = binding.serviceFormInclude.culturalEventsCheckbox;
        CheckBox educationalEventsCheckBox = binding.serviceFormInclude.educationalEventsCheckbox;
        CheckBox sportingEventsCheckBox = binding.serviceFormInclude.sportingEventsCheckbox;
        CheckBox charityEventsCheckBox = binding.serviceFormInclude.charityEventsCheckbox;
        CheckBox politicalEventsCheckBox = binding.serviceFormInclude.politicalEventsCheckbox;

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

        Button submitBtn = binding.serviceFormInclude.submitBtn;
        submitBtn.setOnClickListener(v -> {
            createNewService();

            serviceRepo.createService(newService);
            finish();
        });

    }

    private void createNewService() {
        String serviceName = name.getText().toString();
        String serviceDescription = description.getText().toString();
        String serviceSpecifics = specifics.getText().toString();
        Double serviceDuration = Double.parseDouble(duration.getText().toString());
        int serviceReservationDeadline = Integer.parseInt(reservationDeadline.getText().toString());
        int serviceCancelationDeadline = Integer.parseInt(cancelationDeadline.getText().toString());
        Double servicePrice = Double.parseDouble(price.getText().toString());
        Double serviceDiscount = Double.parseDouble(discount.getText().toString());
        Boolean visible = visibility.isChecked();
        Boolean available = availability.isChecked();
        ReservationMethod reservationMethod = radioButtonAutomatic.isChecked() ? ReservationMethod.automatically : ReservationMethod.manually;

        newService = new Service();
        newService.setCategory(selectedCategory);
        newService.setSubcategory(selectedSubcategory);
        newService.setEmployees(selectedEmployees);
        newService.setSpecifics(serviceSpecifics);
        newService.setServiceDurability(serviceDuration);
        newService.setReservationDeadline(serviceReservationDeadline);
        newService.setCancellationDeadline(serviceCancelationDeadline);
        newService.setPrice(servicePrice);
        newService.setDiscount(serviceDiscount);
        newService.setName(serviceName);
        newService.setDescription(serviceDescription);
        newService.setEventTypes(checkedEventTypes);
        newService.setImages(new ArrayList<>());
        newService.setReservationMethod(reservationMethod);


        LocalDateTime currentTime = LocalDateTime.now();
        newService.setLastChange(currentTime.toString());

        if(suggestedSubcategory){
            newService.setServiceState(ProductState.pending);
        } else {
            newService.setServiceState(ProductState.approved);
        }

        if(visible){
            newService.setVisibility(true);
        } else {
            newService.setVisibility(false);
        }

        if(available){
            newService.setAvailability(true);
        }else {
            newService.setAvailability(false);
        }

    }

    private void showEmployeesPopup(View anchorView, ArrayList<Person> employees) {
        View popupView = getLayoutInflater().inflate(R.layout.employees_selection_popup, null);

        int desiredWidth = 900;
        int desiredHeight = 1000;
        PopupWindow popupWindow = new PopupWindow(
                popupView,
                desiredWidth,
                desiredHeight,
                true
        );

        LinearLayout employeesLayout = popupView.findViewById(R.id.employees_layout);

        for (Person employee : employees) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(employee.getName());
            checkBox.setTag(employee); // Store the Person object as a tag
            employeesLayout.addView(checkBox);
        }

        Button confirmBtn = popupView.findViewById(R.id.confirm_btn);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the selected employees
                for (int i = 0; i < employeesLayout.getChildCount(); i++) {
                    View view = employeesLayout.getChildAt(i);
                    if (view instanceof CheckBox) {
                        CheckBox checkBox = (CheckBox) view;
                        if (checkBox.isChecked()) {
                            Person selectedEmployee = (Person) checkBox.getTag();
                            selectedEmployees.add(selectedEmployee);
                        }
                    }
                }

                popupWindow.dismiss();
            }
        });

        popupWindow.showAsDropDown(anchorView);
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
                subcategorySuggestion.setSubcategoryType("service");
                suggestionRepo.createSuggestion(subcategorySuggestion);
                dialog.dismiss();

            }
        });

        dialog.show();
    }
}