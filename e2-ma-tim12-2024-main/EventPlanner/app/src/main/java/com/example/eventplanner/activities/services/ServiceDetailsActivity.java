package com.example.eventplanner.activities.services;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.eventplanner.R;
import com.example.eventplanner.activities.packages.ReservationForPackageActivity;
import com.example.eventplanner.activities.products.ProductDetailsActivity;
import com.example.eventplanner.adapters.EmployeesNamesAdapter;
import com.example.eventplanner.adapters.EventNameAdapter;
import com.example.eventplanner.databinding.ActivityProductDetailsBinding;
import com.example.eventplanner.databinding.ActivityServiceDetailsBinding;
import com.example.eventplanner.model.Event;
import com.example.eventplanner.model.Package;
import com.example.eventplanner.model.Person;
import com.example.eventplanner.model.Product;
import com.example.eventplanner.model.Service;
import com.example.eventplanner.model.enums.NotificationStatus;
import com.example.eventplanner.model.enums.Role;
import com.example.eventplanner.model.reservations.Reservation;
import com.example.eventplanner.model.reservations.ReservationItem;
import com.example.eventplanner.model.reservations.ReservationStatus;
import com.example.eventplanner.model.system.Notification;
import com.example.eventplanner.repositories.EventRepository;
import com.example.eventplanner.repositories.PersonRepository;
import com.example.eventplanner.repositories.ReservationRepository;
import com.example.eventplanner.repositories.ServiceRepository;
import com.example.eventplanner.repositories.interfaces.IPersonRepository;
import com.example.eventplanner.repositories.system.NotificationRepository;
import com.example.eventplanner.settings.PreferencesManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ServiceDetailsActivity extends AppCompatActivity {

    private EventRepository eventRepository;
    private ServiceRepository serviceRepository;
    private ArrayList<Event> events = new ArrayList<>();
    private String userId;
    private ActivityServiceDetailsBinding binding;
    private String selectedServiceId;
    private Service selectedService;
    private ImageView serviceImage;
    private List<String> imageUrls;
    private int currentImageIndex;
    private Spinner eventsSpinner;
    private Event selectedEvent;
    private View reserveServiceButton;
    private PersonRepository personRepository;
    private Spinner employeesSpinner1;
    private Person selectedEmployee1;
    private Person loggedIn;
    private Person person;
    private ReservationRepository reservationRepository;
    private NotificationRepository notificationRepository;
    private Button addToFavoriteProductButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userId = PreferencesManager.getLoggedUserId(this);
        Log.i("ShopApp", "USER ID KOJI JE: " + userId);
        personRepository = new PersonRepository();
        reservationRepository = new ReservationRepository();
        notificationRepository = new NotificationRepository();
        personRepository.getPersonByUserId(userId, new IPersonRepository.GetPersonsCallback() {
            @Override
            public void OnGetPeople(ArrayList<Person> people) {
                if (people != null && !people.isEmpty()) {
                    loggedIn = people.get(0);
                    Log.i("ShopApp", "USER ID KOJI JE: " + loggedIn.getName());
                } else {
                    Log.d("RequestDetailsActivity", "No person found with the given UserId.");
                }
            }
        });

        EdgeToEdge.enable(this);
        binding = ActivityServiceDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        serviceRepository = new ServiceRepository();

        serviceImage = findViewById(R.id.service_image);

        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("SelectedServiceId")){
            selectedServiceId = intent.getStringExtra("SelectedServiceId");
            Log.i("ShopApp", "Received selectedService: " + selectedServiceId.toString());

            if(selectedServiceId!= null) {
                serviceRepository.getById(new ServiceRepository.ServiceByIdFetchCallback() {
                    @Override
                    public void onServiceByIdFetch(Service fetchedService) {
                        if (fetchedService != null) {
                            selectedService = fetchedService;

                            imageUrls = selectedService.getImages();
                            currentImageIndex = 0;
                            showImage(currentImageIndex);

                            binding.serviceName.setText(selectedService.getName());
                            binding.serviceDescription.setText("Description: " + selectedService.getDescription());
                            if(selectedService.getCategory() != null) {
                                binding.serviceCategory.setText("Category: " + selectedService.getCategory().getName());
                            }
                            if(selectedService.getSubcategory() != null) {
                                binding.serviceSubcategory.setText("Subcategory: " + selectedService.getSubcategory().getName());
                            }
                            binding.serviceTypes.setText("Event types: " + TextUtils.join(", ", selectedService.getEventTypes()));
                            binding.servicePrice.setText("Price: " + selectedService.getPrice() + " $");
                            binding.serviceDiscount.setText("Discount: " + selectedService.getDiscount() + "%");
                            binding.serviceAvailability.setText(selectedService.isAvailability() ? "Available" : "Not Available");
                            binding.serviceSpecifics.setText("Specifics: " + selectedService.getSpecifics());
                            binding.serviceDuration.setText("Duration: " + selectedService.getServiceDurability());
                            binding.serviceReservationDeadline.setText("Reservation deadline: " + selectedService.getReservationDeadline());
                            binding.serviceCancellationDeadline.setText("Deadline for cancellation: " + selectedService.getCancellationDeadline());
                            SetupVisibility();
                            setupEmployeeSpinner(selectedService);

                            reserveServiceButton = findViewById(R.id.buyProductButton);
                            reserveServiceButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    reserveService(selectedEvent, selectedService, selectedEmployee1);
                                }
                            });

                            addToFavoriteProductButton = binding.addToFavoritesButton;
                            addToFavoriteProductButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    addToFavouritesService(selectedService);
                                }
                            });
                            Log.d("EVENT NEKI", "USLUGA STIGLA" + selectedService.getId());
                        }

                    }
                }, selectedServiceId);
            }
        }

        eventRepository = new EventRepository();
        eventsSpinner = binding.spinEvent;
        ArrayList<Event> eventsSpin = new ArrayList<>();
        eventRepository.getEventsByOrganiserId(userId, new EventRepository.EventFetchCallback(){
            @Override
            public void onEventFetch(ArrayList<Event> fetchedEvents) {
                if (fetchedEvents != null){
                    eventsSpin.addAll(fetchedEvents);
                    EventNameAdapter eventAdapter = new EventNameAdapter(ServiceDetailsActivity.this, android.R.layout.simple_spinner_item, eventsSpin);
                    eventAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    eventsSpinner.setAdapter(eventAdapter);
                    eventsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            selectedEvent = eventsSpin.get(position);
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }
            }
        });
    }

    private void addToFavouritesService(Service service) {
        personRepository.getPersonByUserId(userId,  new IPersonRepository.GetPersonsCallback() {
            @Override
            public void OnGetPeople(ArrayList<Person> people) {
                if (people != null && !people.isEmpty()) {
                    person = people.get(0);
                    if (person != null && service != null) {

                        person.addFavouriteService(service);
                        personRepository.updateForFavouriteList(person.getId(), person);
                        Toast.makeText(ServiceDetailsActivity.this, "Added to favorites", Toast.LENGTH_SHORT).show();
                        finish();
                        Log.d("UserProfileActivity", "Person found: " + person.toString());
                    } else {
                        Log.d("UserProfileActivity", "Person is null.");
                    }
                } else {
                    Log.d("UserProfileActivity", "No person found with the given UserId.");
                }
            }
        });
    }


    private void reserveService(Event event, Service service, Person selectedEmployee1) {
        if (event != null && service != null) {
            Reservation reservation = new Reservation();
            reservation.setOrganizatorId(PreferencesManager.getLoggedUserEmail(ServiceDetailsActivity.this));
            reservation.setPackageId("");
            reservation.setOrganizatorName(loggedIn.getName());
            reservation.setOrganizatorLastname(loggedIn.getLastname());
            reservation.setStatus(ReservationStatus.newReservation);
            reservation.setReservationDate(new Date());
            reservation.setPackage(false);

            ReservationItem reservationItem = new ReservationItem();
            reservationItem.setServiceId(service.getId());
            reservationItem.setServiceName(service.getName());
            reservationItem.setEmployeeId(selectedEmployee1.getUserId());
            reservationItem.setEmployeeName(selectedEmployee1.getName());
            reservationItem.setEmployeeLastname(selectedEmployee1.getLastname());

            reservation.setItem(reservationItem);
            reservationRepository.Create(reservation);

            Log.i("REZERVACIJA", reservation.toString());


            String organizatorEmail = loggedIn.getEmail();
            String organizatorId = loggedIn.getUserId();
            String employeeId = selectedEmployee1.getUserId();

            Notification forEmployee = new Notification();
            forEmployee.setSenderId(organizatorId);
            forEmployee.setSenderEmail(organizatorEmail);
            forEmployee.setReceiverId(employeeId);
            forEmployee.setTitle("New service reservation");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy, HH:mm");
            String formattedDate = dateFormat.format(new Date());
            forEmployee.setText("User " + loggedIn.getName() + " " + loggedIn.getLastname() + " has reserved your service for " + event.getDate());
            forEmployee.setStatus(NotificationStatus.unread);
            notificationRepository.Create(forEmployee);

            Notification forOrganizator = new Notification();
            forOrganizator.setSenderId(organizatorId);
            forOrganizator.setSenderEmail(organizatorEmail);
            forOrganizator.setReceiverId(organizatorId);
            forOrganizator.setTitle("Successful reservation");
            forOrganizator.setText("Successful reservation for service: " + service.getName() + " for " + event.getDate());
            forOrganizator.setStatus(NotificationStatus.unread);
            notificationRepository.Create(forOrganizator);


            Toast.makeText(ServiceDetailsActivity.this, "Bravo", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Log.e("ProductDetailsActivity", "Event or Product is null.");
        }
    }
    private void setupEmployeeSpinner(Service service) {
        employeesSpinner1 = binding.spinEmployee1;
        ArrayList<Person> employeesSpin1 = new ArrayList<>();

        // Ensure service and its employees list are not null before accessing them
        if (service != null && service.getEmployees() != null) {
            employeesSpin1.addAll(service.getEmployees());
            EmployeesNamesAdapter employeesNamesAdapter1 = new EmployeesNamesAdapter(ServiceDetailsActivity.this, android.R.layout.simple_spinner_item, employeesSpin1);
            employeesNamesAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            employeesSpinner1.setAdapter(employeesNamesAdapter1);
            employeesSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedEmployee1 = employeesSpin1.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        } else {
            Log.w("ServiceDetailsActivity", "Service or employees are null or empty.");
        }
    }

    private void SetupVisibility() {
        // Check user role and product availability to show or hide views
        if (PreferencesManager.getLoggedUserRole(this) == Role.organizator && selectedService.isAvailability()) {
            binding.productEvent.setVisibility(View.VISIBLE);
            binding.spinEvent.setVisibility(View.VISIBLE);
            binding.buyProductButton.setVisibility(View.VISIBLE);
            binding.spinEmployee1.setVisibility(View.VISIBLE);
            binding.employee1.setVisibility(View.VISIBLE);
            binding.buyProductButton.setVisibility(View.VISIBLE);
        } else {
            binding.productEvent.setVisibility(View.GONE);
            binding.spinEvent.setVisibility(View.GONE);
            binding.buyProductButton.setVisibility(View.GONE);
            binding.spinEmployee1.setVisibility(View.GONE);
            binding.employee1.setVisibility(View.GONE);
            binding.buyProductButton.setVisibility(View.GONE);
        }

        if(PreferencesManager.getLoggedUserRole(this) == Role.organizator) {
            binding.addToFavoritesButton.setVisibility(View.VISIBLE);
        }
        else {
            binding.addToFavoritesButton.setVisibility(View.GONE);
        }
    }
    private void showImage(int index) {
        if (imageUrls != null && index >= 0 && index < imageUrls.size()) {
            Glide.with(this)
                    .load(imageUrls.get(index))
                    .into(serviceImage);
        }
    }

    public void showPreviousImage(View view) {
        if (currentImageIndex > 0) {
            currentImageIndex--;
            showImage(currentImageIndex);
        }
    }

    public void showNextImage(View view) {
        if (imageUrls != null && currentImageIndex < imageUrls.size() - 1) {
            currentImageIndex++;
            showImage(currentImageIndex);
        }
    }
}