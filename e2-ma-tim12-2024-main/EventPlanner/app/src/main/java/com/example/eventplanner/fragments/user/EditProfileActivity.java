package com.example.eventplanner.fragments.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventplanner.R;
import com.example.eventplanner.databinding.ActivityEditProfileBinding;
import com.example.eventplanner.databinding.ActivityUserProfileBinding;
import com.example.eventplanner.databinding.FragmentUserProfileBinding;
import com.example.eventplanner.model.Person;
import com.example.eventplanner.model.User;
import com.example.eventplanner.model.enums.Role;
import com.example.eventplanner.repositories.PersonRepository;
import com.example.eventplanner.repositories.UserReportRepository;
import com.example.eventplanner.repositories.UserRepository;
import com.example.eventplanner.repositories.interfaces.IPersonRepository;
import com.example.eventplanner.repositories.system.NotificationRepository;
import com.example.eventplanner.settings.PreferencesManager;

import java.util.ArrayList;
import java.util.List;

public class EditProfileActivity extends AppCompatActivity {

    private ActivityEditProfileBinding binding;
    private EditText name;
    private EditText lastName;
    private EditText address;
    private EditText email;
    private EditText phone;
    private EditText password;
    private PersonRepository personRepository;
    private UserRepository userRepository;
    private UserReportRepository userReportRepository;
    private NotificationRepository notificationRepository;
    private List<User> admins = new ArrayList<>();
    private String personId;
    private Person person;
    private String userId;
    private String userEmail;
    private Role userRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        userId = PreferencesManager.getLoggedUserId(this);
        userRole = PreferencesManager.getLoggedUserRole(this);

        binding = ActivityEditProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        personRepository = new PersonRepository();

        personRepository.getPersonByUserId(userId,  new IPersonRepository.GetPersonsCallback() {
            @Override
            public void OnGetPeople(ArrayList<Person> people) {
                if (people != null && !people.isEmpty()) {
                    person = people.get(0);
                    if (person != null) {
                        name = binding.userName;
                        name.setText(String.valueOf(person.getName()));
                        lastName = binding.userLastname;
                        lastName.setText(String.valueOf(person.getLastname()));
                        email = binding.userEmail;
                        email.setText(String.valueOf(person.getEmail()));
                        address = binding.userAddress;
                        address.setText(String.valueOf(person.getAddress()));
                        phone = binding.userPhone;
                        phone.setText(String.valueOf(person.getPhoneNumber()));
                        Log.d("UserProfileActivity", "Person found: " + person.toString());
                    } else {
                        Log.d("UserProfileActivity", "Person is null.");
                    }
                } else {
                    Log.d("UserProfileActivity", "No person found with the given UserId.");
                }
            }
        });

        Button updateBtn = binding.updateProfileButton;
        updateBtn.setOnClickListener(v -> {
            if(person !=null) {
                String name = binding.userName.getText().toString();
                String lastName = binding.userLastname.getText().toString();
                String email = binding.userEmail.getText().toString();
                String address = binding.userAddress.getText().toString();
                String phone = binding.userPhone.getText().toString();

                person.setName(name);
                person.setName(lastName);
                person.setEmail(email);
                person.setAddress(address);
                person.setPhoneNumber(phone);

                personRepository.Update(person.getId(), person);
                binding.userName.setText(String.valueOf(person.getName()));
                binding.userLastname.setText(String.valueOf(person.getLastname()));
                binding.userEmail.setText(String.valueOf(person.getEmail()));
                binding.userAddress.setText(String.valueOf(person.getAddress()));
                binding.userPhone.setText(String.valueOf(person.getPhoneNumber()));

            }
        });

        return root;
    }
}