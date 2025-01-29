package com.example.eventplanner.activities.regRequests;

import static java.security.AccessController.getContext;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventplanner.R;
import com.example.eventplanner.databinding.ActivityRequestDetailsBinding;
import com.example.eventplanner.fragments.stakeholders.EmployeeRegistrationFragment;
import com.example.eventplanner.model.Category;
import com.example.eventplanner.model.Company;
import com.example.eventplanner.model.EventType;
import com.example.eventplanner.model.Person;
import com.example.eventplanner.model.Subcategory;
import com.example.eventplanner.model.User;
import com.example.eventplanner.repositories.CompanyRepository;
import com.example.eventplanner.repositories.PersonRepository;
import com.example.eventplanner.repositories.UserRepository;
import com.example.eventplanner.repositories.interfaces.IPersonRepository;
import com.example.eventplanner.repositories.interfaces.IUserRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class RequestDetailsActivity extends AppCompatActivity {

    private ActivityRequestDetailsBinding binding;
    private Company company;
    private CompanyRepository companyRepository;
    private PersonRepository personRepository;
    private UserRepository userRepository;
    private Person person;
    private EditText declineReasonEditText;
    private Button confirmDeclineButton;
    private String oPassword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRequestDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        companyRepository = new CompanyRepository();
        personRepository = new PersonRepository();
        userRepository = new UserRepository();

        Log.d("RequestDetailsActivity", "onCreate started.");

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("company")) {
            Log.d("RequestDetailsActivity", "Intent is not null and contains 'company' extra.");

            company = intent.getParcelableExtra("company");
            if (company != null) {
                Log.i("RequestDetailsActivity", "Received Company: " + company.toString());

                // Log categories
                if (company.getCategories() != null) {
                    Log.i("RequestDetailsActivity", "Company categories size: " + company.getCategories().size());
                    for (int i = 0; i < company.getCategories().size(); i++) {
                        Log.i("RequestDetailsActivity", "Category " + i + ": " + company.getCategories().get(i).getName());
                    }
                } else {
                    Log.i("RequestDetailsActivity", "Company categories are null.");
                }

                personRepository.getPersonByUserId(company.getOwnerId(), new IPersonRepository.GetPersonsCallback() {
                    @Override
                    public void OnGetPeople(ArrayList<Person> people) {
                        if (people != null && !people.isEmpty()) {
                            person = people.get(0);
                            if (person != null) {
                                binding.personPhone.setText("Phone: " + person.getPhoneNumber());
                                binding.personAddress.setText("Address: " + person.getAddress());
                                Log.d("RequestDetailsActivity", "Person found: " + person.toString());
                            } else {
                                Log.d("RequestDetailsActivity", "Person is null.");
                            }
                        } else {
                            Log.d("RequestDetailsActivity", "No person found with the given UserId.");
                        }
                    }
                });

                binding.personName.setText("Name: " + company.getOwnerName());
                binding.personLastname.setText("Lastname: " + company.getOwnerLastname());
                binding.personEmail.setText("Email: " + company.getOwnerEmail());
                binding.companyName.setText("Name: " + company.getName());
                binding.companyDescription.setText("Description: " + company.getDescrtiption());
                binding.companyEmail.setText("Email: " + company.getEmail());
                binding.companyAddress.setText("Address: " + company.getCompanyAdress());
                binding.companyPhone.setText("Phone: " + company.getPhoneNumber());

                // Prikazivanje naziva podkategorija
                if (company.getCategories() !=null && !company.getCategories().isEmpty()){
                    StringBuilder categoryNames = new StringBuilder();
                    for (Category category : company.getCategories()){
                        categoryNames.append(category.getName()).append(", ");
                    }
                    categoryNames.deleteCharAt(categoryNames.length() - 2);
                    binding.categories.setText("Categories: " + categoryNames.toString());
                }else{
                    binding.categories.setText(" ");
                }

                // Prikazivanje naziva tipova dogadjaja
                if (company.getEventTypes() !=null && !company.getEventTypes().isEmpty()){
                    StringBuilder eventNames = new StringBuilder();
                    for (EventType e : company.getEventTypes()){
                        eventNames.append(e.getName()).append(", ");
                    }
                    eventNames.deleteCharAt(eventNames.length() - 2);
                    binding.eventTypes.setText("Event types: " + eventNames.toString());
                }else{
                    binding.eventTypes.setText(" ");
                }


            } else {
                Log.d("RequestDetailsActivity", "Received company is null.");
            }
        } else {
            Log.d("RequestDetailsActivity", "Intent is null or does not have 'company' extra.");
        }

        Log.d("RequestDetailsActivity", "onCreate finished.");



        Button approveBtn = binding.approveBtn;
        Button declineBtn = binding.declineBtn;
        declineReasonEditText = findViewById(R.id.decline_reason);
        confirmDeclineButton = findViewById(R.id.confirm_decline_btn);

        userRepository.GetByEmail(company.getOwnerEmail(), new IUserRepository.GetUsersCallback() {
            @Override
            public void OnGetUsers(ArrayList<User> users) {
                IUserRepository.GetUsersCallback.super.OnGetUsers(users);
                if(users.isEmpty()) return;
                oPassword = users.get(0).getPassword();
                Log.i("LOZINKA", oPassword);
            }
        });

        approveBtn.setOnClickListener(v -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(RequestDetailsActivity.this);
            dialog.setMessage("Are you sure you want to approve the registration for " + company.getOwnerName() + "?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                            Log.d("Employee_reg_msg", "Started registration");
                            Log.i("pass", oPassword);
                            firebaseAuth.createUserWithEmailAndPassword(company.getOwnerEmail(), oPassword)

                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                // User registration successful
                                                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                                                if (firebaseUser != null) {
                                                    firebaseUser.sendEmailVerification()
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        Log.d("Owner_reg_msg", "Verification email sent.");
                                                                    } else {
                                                                        Log.e("Owner_reg_msg", "Failed to send verification email.", task.getException());
                                                                    }
                                                                }
                                                            });
                                                    company.setRequestHandled(true);
                                                    companyRepository.Update(company);
                                                    Log.d("Owner_reg_msg", "User and Person created in repositories.");
                                                    //Toast.makeText(EmployeeRegistrationFragment.this.getContext(), "Employee registration successful", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Log.e("Owner_reg_msg", "FirebaseUser is null after registration.");
                                                }
                                            } else {
                                                // Registration failed
                                                Log.e("Employee_reg_msg", "User registration failed.", task.getException());
                                                //Toast.makeText(EmployeeRegistrationFragment.this.getContext(), "Employee registration unsuccessful", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                            dialog.dismiss();
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = dialog.create();
            alert.show();
        });


        declineBtn.setOnClickListener(v -> {
            declineReasonEditText.setVisibility(View.VISIBLE);
            confirmDeclineButton.setVisibility(View.VISIBLE);
        });

        confirmDeclineButton.setOnClickListener(v -> {
            String declineReason = declineReasonEditText.getText().toString().trim();
            if (declineReason.isEmpty()) {
                Toast.makeText(RequestDetailsActivity.this, "Please provide a reason for decline", Toast.LENGTH_SHORT).show();
                return;
            }

            // Handle the decline logic here
            company.setRequestHandled(true);
            companyRepository.Update(company);

            Log.d("RequestDetailsActivity", "Company declined with reason: " + declineReason);
            Toast.makeText(RequestDetailsActivity.this, "Company declined", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
