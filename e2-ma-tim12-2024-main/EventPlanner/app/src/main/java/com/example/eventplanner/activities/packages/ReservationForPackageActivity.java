package com.example.eventplanner.activities.packages;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eventplanner.R;
import com.example.eventplanner.activities.products.ProductDetailsActivity;
import com.example.eventplanner.activities.services.ServiceDetailsActivity;
import com.example.eventplanner.adapters.EmployeesNamesAdapter;
import com.example.eventplanner.adapters.EventNameAdapter;
import com.example.eventplanner.databinding.ActivityReservationForPackageBinding;
import com.example.eventplanner.model.Category;
import com.example.eventplanner.model.Event;
import com.example.eventplanner.model.Package;
import com.example.eventplanner.model.Person;
import com.example.eventplanner.model.Product;
import com.example.eventplanner.model.Service;
import com.example.eventplanner.model.enums.NotificationStatus;
import com.example.eventplanner.model.reservations.Reservation;
import com.example.eventplanner.model.reservations.ReservationItem;
import com.example.eventplanner.model.reservations.ReservationStatus;
import com.example.eventplanner.model.system.Notification;
import com.example.eventplanner.repositories.EventRepository;
import com.example.eventplanner.repositories.PackageRepository;
import com.example.eventplanner.repositories.PersonRepository;
import com.example.eventplanner.repositories.ReservationRepository;
import com.example.eventplanner.repositories.interfaces.IPersonRepository;
import com.example.eventplanner.repositories.system.NotificationRepository;
import com.example.eventplanner.settings.PreferencesManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReservationForPackageActivity extends AppCompatActivity {

    private ActivityReservationForPackageBinding binding;
    private Package packageForRes;
    private PackageRepository packageRepository;
    private EventRepository eventRepository;
    private PersonRepository personRepository;
    private Spinner eventsSpinner;
    private Spinner employeesSpinner1;
    private Spinner employeesSpinner2;
    private Event selectedEvent;
    private Person selectedEmployee1;
    private Person selectedEmployee2;
    private String userId;
    private View buyReservePackageButton;
    private Person loggedIn;
    private ReservationRepository reservationRepository;
    private NotificationRepository notificationRepository;
    private CheckBox cbReject1;
    private CheckBox cbReject2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = PreferencesManager.getLoggedUserId(this);
        Log.i("ShopApp", "USER ID KOJI JE: " + userId);

        binding = ActivityReservationForPackageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        cbReject1 = findViewById(R.id.cbReject1);
        cbReject2 = findViewById(R.id.cbReject2);

        cbReject1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                employeesSpinner1.setVisibility(View.GONE);
                cbReject1.setVisibility(View.VISIBLE);
            } else {
                employeesSpinner1.setVisibility(View.VISIBLE);
                cbReject1.setVisibility(View.VISIBLE);
            }
        });

        cbReject2.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                employeesSpinner2.setVisibility(View.GONE);
                cbReject2.setVisibility(View.VISIBLE);
            } else {
                employeesSpinner2.setVisibility(View.VISIBLE);
                cbReject2.setVisibility(View.VISIBLE);
            }
        });

        packageRepository = new PackageRepository();
        eventRepository = new EventRepository();

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

        Log.d("ReservationForPackageActivity", "onCreate started.");

        String packageId = "package_1717844812399";
        packageRepository.getPackageById(packageId, new PackageRepository.PackageFetchCallback() {
            @Override
            public void onPackageFetch(ArrayList<Package> packages) {
                if (packages != null && !packages.isEmpty()) {
                    packageForRes = packages.get(0);
                    Log.d("ReservationForPackageActivity", "Package fetched: " + packageForRes.getId());
                    // Do something with the package, e.g., display details in the UI
                    binding.packageName.setText("Naziv: " + packageForRes.getName());

                    // Prikazivanje naziva podkategorija
                    if (packageForRes.getmProducts() !=null && !packageForRes.getmProducts().isEmpty()){
                        StringBuilder prodNames = new StringBuilder();
                        for (Product product : packageForRes.getmProducts()){
                            prodNames.append(product.getName()).append(", ");
                        }
                        prodNames.deleteCharAt(prodNames.length() - 2);
                        binding.packageProducts.setText(prodNames.toString());
                    }else{
                        binding.packageProducts.setText(" ");
                    }


                    binding.packageService1.setText(packageForRes.getmServices().get(0).getName());
                    setupEmployeeSpinner(packageForRes);

                    binding.packageService2.setText(packageForRes.getmServices().get(1).getName());
                    setupEmployeeSpinner2(packageForRes);

                    buyReservePackageButton = findViewById(R.id.buyConfirmButton);
                    buyReservePackageButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            reservePackage(selectedEvent, packageForRes.getmProducts(), packageForRes.getmServices(), selectedEmployee1, selectedEmployee2, packageForRes.getId(), packageForRes.getName());
                        }
                    });

                } else {
                    Log.d("ReservationForPackageActivity", "No package found with ID: " + packageId);
                }
            }
        });

        eventsSpinner = binding.spinEvent;
        ArrayList<Event> eventsSpin = new ArrayList<>();
        eventRepository.getEventsByOrganiserId(userId, new EventRepository.EventFetchCallback(){
            @Override
            public void onEventFetch(ArrayList<Event> fetchedEvents) {
                if (fetchedEvents != null){
                    eventsSpin.addAll(fetchedEvents);
                    EventNameAdapter eventAdapter = new EventNameAdapter(ReservationForPackageActivity.this, android.R.layout.simple_spinner_item, eventsSpin);
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

    private void reservePackage(Event event, List<Product> products, List<Service> services, Person selectedEmployee1, Person selectedEmployee2, String packageId, String packageName) {
        if (event != null && products != null && !products.isEmpty()) {
            // Initialize the list of bought products if it doesn't exist
            if (event.getBoughtProducts() == null) {
                event.setBoughtProducts(new ArrayList<>());
            }

            // Iterate through the list of products and add each one to the event's bought products
            for (Product product : products) {
                event.getBoughtProducts().add(product);
            }

            // Log the list of bought products
            Log.i("Proizvodi", event.getBoughtProducts().toString());

            // Update the event in the repository
            eventRepository.update(event);
            // Display a success message
            Toast.makeText(ReservationForPackageActivity.this, "Bravo", Toast.LENGTH_SHORT).show();

            // Check if the first service should be reserved
            if (!cbReject1.isChecked()) {
                reserveService(event, services.get(0), selectedEmployee1, packageId, event.getDate());
            }

            // Check if the second service should be reserved
            if (!cbReject2.isChecked()) {
                reserveService(event, services.get(1), selectedEmployee2, packageId, event.getDate());
            }

            String organizatorEmail = loggedIn.getEmail();
            String organizatorId = loggedIn.getUserId();
            Notification forOrganizator = new Notification();
            forOrganizator.setSenderId(organizatorId);
            forOrganizator.setSenderEmail(organizatorEmail);
            forOrganizator.setReceiverId(organizatorId);
            forOrganizator.setTitle("Successful package reservation");
            forOrganizator.setText("Successful reservation for service: " + packageName + " for " + event.getDate());
            forOrganizator.setStatus(NotificationStatus.unread);
            notificationRepository.Create(forOrganizator);

            finish();
        } else {
            Log.e("ProductDetailsActivity", "Event or Products is null.");
        }
    }

    private void reserveService(Event event, Service service, Person selectedEmployee1, String packageId, String date) {
        if (event != null && service != null) {
            Reservation reservation = new Reservation();
            reservation.setOrganizatorId(PreferencesManager.getLoggedUserEmail(ReservationForPackageActivity.this));
            reservation.setPackageId(packageId);
            reservation.setOrganizatorName(loggedIn.getName());
            reservation.setOrganizatorLastname(loggedIn.getLastname());
            reservation.setStatus(ReservationStatus.newReservation);
            reservation.setReservationDate(new Date());
            reservation.setPackage(true);

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
            forEmployee.setText("User " + loggedIn.getName() + " " + loggedIn.getLastname() + " has reserved your service for " + date);
            forEmployee.setStatus(NotificationStatus.unread);
            notificationRepository.Create(forEmployee);

            Toast.makeText(ReservationForPackageActivity.this, "Bravo", Toast.LENGTH_SHORT).show();
            //finish();
        } else {
            Log.e("ProductDetailsActivity", "Event or Product is null.");
        }
    }

    private void setupEmployeeSpinner(Package packageForRes) {
        employeesSpinner1 = binding.spinEmployee1;
        ArrayList<Person> employeesSpin1 = new ArrayList<>();

        // Ensure packageForRes and its services are not null before accessing them
        if (packageForRes != null && packageForRes.getmServices() != null && !packageForRes.getmServices().isEmpty()) {
            employeesSpin1.addAll(packageForRes.getmServices().get(0).getEmployees());
            EmployeesNamesAdapter employeesNamesAdapter1 = new EmployeesNamesAdapter(ReservationForPackageActivity.this, android.R.layout.simple_spinner_item, employeesSpin1);
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
            Log.w("ReservationForPackageActivity", "Package services or employees are null or empty.");
        }
    }

    private void setupEmployeeSpinner2(Package packageForRes) {
        employeesSpinner2 = binding.spinEmployee2;
        ArrayList<Person> employeesSpin2 = new ArrayList<>();

        // Ensure packageForRes and its services are not null before accessing them
        if (packageForRes != null && packageForRes.getmServices() != null && !packageForRes.getmServices().isEmpty()) {
            employeesSpin2.addAll(packageForRes.getmServices().get(1).getEmployees());
            EmployeesNamesAdapter employeesNamesAdapter2 = new EmployeesNamesAdapter(ReservationForPackageActivity.this, android.R.layout.simple_spinner_item, employeesSpin2);
            employeesNamesAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            employeesSpinner2.setAdapter(employeesNamesAdapter2);
            employeesSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedEmployee2 = employeesSpin2.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        } else {
            Log.w("ReservationForPackageActivity", "Package services or employees are null or empty.");
        }
    }
}