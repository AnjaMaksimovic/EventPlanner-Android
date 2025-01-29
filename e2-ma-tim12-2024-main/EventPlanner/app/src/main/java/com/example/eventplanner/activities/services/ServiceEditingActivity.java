package com.example.eventplanner.activities.services;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventplanner.R;
import com.example.eventplanner.adapters.SubcategoryNameAdapter;
import com.example.eventplanner.databinding.ActivityServiceCreatingBinding;
import com.example.eventplanner.databinding.ActivityServiceEditingBinding;
import com.example.eventplanner.model.Category;
import com.example.eventplanner.model.Person;
import com.example.eventplanner.model.Service;
import com.example.eventplanner.model.Subcategory;
import com.example.eventplanner.model.enums.EventType;
import com.example.eventplanner.repositories.CategoryRepository;
import com.example.eventplanner.repositories.PersonRepository;
import com.example.eventplanner.repositories.ProductRepository;
import com.example.eventplanner.repositories.ServiceRepository;
import com.example.eventplanner.repositories.SubcategoryRepository;
import com.example.eventplanner.repositories.SubcategorySuggestionRepository;
import com.example.eventplanner.repositories.interfaces.IPersonRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServiceEditingActivity extends AppCompatActivity {
    private ActivityServiceEditingBinding binding;
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
    private SubcategorySuggestionRepository suggestionRepo;
    private PersonRepository personRepo;
    private Subcategory selectedSubcategory;
    private List<Person> selectedEmployees = new ArrayList<>();
    private Category selectedCategory;
    private Service service;
    private boolean suggestedSubcategory;
    private Button suggestionBtn;

    private RadioButton radioButtonManually;
    private RadioButton radioButtonAutomatic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityServiceEditingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        subcategoryRepo = new SubcategoryRepository();
        serviceRepo = new ServiceRepository();
        personRepo = new PersonRepository();
        subcategorySpinner = binding.serviceEditFormInclude.btnSubcategory;

        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("service")){
            service = intent.getParcelableExtra("service");
            Log.i("ShopApp", "Received Service: " + service.toString());

            EditText categoryEditText = binding.serviceEditFormInclude.categoryEditText;
            categoryEditText.setText(service.getCategory().getName()); // Assuming getCategory() returns category text

            name = binding.serviceEditFormInclude.nameField;
            name.setText(service.getName());

            description = binding.serviceEditFormInclude.descriptionField;
            description.setText(service.getDescription());

            price = binding.serviceEditFormInclude.priceField;
            price.setText(String.valueOf(service.getPrice())); // Assuming getPrice() returns double or int

            discount = binding.serviceEditFormInclude.discountField;
            discount.setText(String.valueOf(service.getDiscount()));

            duration = binding.serviceEditFormInclude.durationField;
            duration.setText(String.valueOf(service.getServiceDurability()));

            reservationDeadline = binding.serviceEditFormInclude.reservationDeadlineField;
            reservationDeadline.setText(String.valueOf(service.getReservationDeadline()));

            cancelationDeadline = binding.serviceEditFormInclude.reservationDeadlineField;
            cancelationDeadline.setText(String.valueOf(service.getCancellationDeadline()));

            specifics = binding.serviceEditFormInclude.specificsField;
            specifics.setText(String.valueOf(service.getSpecifics()));

            setCheckBoxChecked();

            availability = findViewById(R.id.availability_checkbox);
            if(service.isAvailability()){
                availability.setChecked(true);
            } else {
                availability.setChecked(false);
            }

            visibility = findViewById(R.id.visibility_checkbox);
            if(service.isVisibility()){
                visibility.setChecked(true);
            } else {
                visibility.setChecked(false);
            }
        }

        setNewTypeEvents();

        ArrayList<Subcategory> subcategories = new ArrayList<>();
        Subcategory initialSubcategory = service.getSubcategory();

        subcategoryRepo.getSubcategoriesByCategoryId(service.getCategory().getId(), new SubcategoryRepository.SubcategoryFetchCallback() {
            @Override
            public void onSubcategoryFetch(ArrayList<Subcategory> fetchedSubcategories) {
                if (fetchedSubcategories != null && !fetchedSubcategories.isEmpty()){

                    subcategories.addAll(fetchedSubcategories);

                    SubcategoryNameAdapter subcategoryAdapter = new SubcategoryNameAdapter(ServiceEditingActivity.this, android.R.layout.simple_spinner_item, fetchedSubcategories);
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

        ArrayList<Person> employees = new ArrayList<>();

        personRepo.GetAllByCompany("Company_2", new IPersonRepository.GetPersonsCallback() {
            @Override
            public void OnGetPeople(ArrayList<Person> persons) {
                employees.addAll(persons);
            }
        });

        Button selectEmployeesButton = binding.serviceEditFormInclude.employeesSelectBtn;
        selectEmployeesButton.setOnClickListener(v -> {
            showEmployeesPopup(v, employees);
        });

        Button submitBtn = binding.serviceEditFormInclude.submitBtn;
        submitBtn.setOnClickListener(v -> {
            editService();
            serviceRepo.updateService(service);
            finish();
        });


        ImageButton backBtn = binding.serviceEditFormInclude.btnBack;
        backBtn.setOnClickListener(v -> {
            finish();
        });
    }

    private void setCheckBoxChecked() {
        CheckBox privateEventsCheckBox = findViewById(R.id.private_events_checkbox);
        if (service.getEventTypes().contains(EventType.private_events)) {
            privateEventsCheckBox.setChecked(true);
        }

        CheckBox corporateEventsCheckBox = findViewById(R.id.corporate_events_checkbox);
        if (service.getEventTypes().contains(EventType.corporate)) {
            corporateEventsCheckBox.setChecked(true);
        }

        CheckBox culturalEventsCheckBox = findViewById(R.id.cultural_events_checkbox);
        if (service.getEventTypes().contains(EventType.cultural)) {
            culturalEventsCheckBox.setChecked(true);
        }

        CheckBox charityEventsCheckBox = findViewById(R.id.charity_events_checkbox);
        if (service.getEventTypes().contains(EventType.charity)) {
            charityEventsCheckBox.setChecked(true);
        }

        CheckBox politicalEventsCheckBox = findViewById(R.id.political_events_checkbox);
        if (service.getEventTypes().contains(EventType.political)) {
            politicalEventsCheckBox.setChecked(true);
        }

        CheckBox sportingEventsCheckBox = findViewById(R.id.sporting_events_checkbox);
        if (service.getEventTypes().contains(EventType.sporting)) {
            sportingEventsCheckBox.setChecked(true);
        }

        CheckBox educationalEventsCheckBox = findViewById(R.id.educational_events_checkbox);
        if (service.getEventTypes().contains(EventType.educational)) {
            educationalEventsCheckBox.setChecked(true);
        }
    }

    private void setNewTypeEvents(){
        CheckBox privateEventsCheckBox = binding.serviceEditFormInclude.privateEventsCheckbox;
        CheckBox corporateEventsCheckBox = binding.serviceEditFormInclude.corporateEventsCheckbox;
        CheckBox culturalEventsCheckBox = binding.serviceEditFormInclude.culturalEventsCheckbox;
        CheckBox educationalEventsCheckBox = binding.serviceEditFormInclude.educationalEventsCheckbox;
        CheckBox sportingEventsCheckBox = binding.serviceEditFormInclude.sportingEventsCheckbox;
        CheckBox charityEventsCheckBox = binding.serviceEditFormInclude.charityEventsCheckbox;
        CheckBox politicalEventsCheckBox = binding.serviceEditFormInclude.politicalEventsCheckbox;

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
            checkBox.setTag(employee);
            employeesLayout.addView(checkBox);

            if (service.getEmployees() != null && service.getEmployees().stream()
                    .map(Person::getId)
                    .anyMatch(id -> id.equals(employee.getId()))) {
                checkBox.setChecked(true);
            }
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

    private void editService() {
        String serviceName = name.getText().toString();
        String serviceDescription = description.getText().toString();
        String serviceSpecifics = specifics.getText().toString();
        Double servicePrice = Double.parseDouble(price.getText().toString());
        Double serviceDiscount = Double.parseDouble(discount.getText().toString());
        Double serviceDuration = Double.parseDouble(duration.getText().toString());
        Integer serviceReservationDeadline = Integer.parseInt(reservationDeadline.getText().toString());
        Integer serviceCancelationDeadline = Integer.parseInt(cancelationDeadline.getText().toString());
        Boolean visible = visibility.isChecked();
        Boolean available = availability.isChecked();

        service.setSubcategory(selectedSubcategory);
        service.setPrice(servicePrice);
        service.setSpecifics(serviceSpecifics);
        service.setEmployees(selectedEmployees);
        service.setDiscount(serviceDiscount);
        service.setName(serviceName);
        service.setDescription(serviceDescription);
        service.setEventTypes(checkedEventTypes);
        service.setServiceDurability(serviceDuration);
        service.setReservationDeadline(serviceReservationDeadline);
        service.setCancellationDeadline(serviceCancelationDeadline);
        service.setImages(new ArrayList<>());

        LocalDateTime currentTime = LocalDateTime.now();
        service.setLastChange(currentTime.toString());


        if(visible){
            service.setVisibility(true);
        } else {
            service.setVisibility(false);
        }

        if(available){
            service.setAvailability(true);
        }else {
            service.setAvailability(false);
        }

    }
    
}