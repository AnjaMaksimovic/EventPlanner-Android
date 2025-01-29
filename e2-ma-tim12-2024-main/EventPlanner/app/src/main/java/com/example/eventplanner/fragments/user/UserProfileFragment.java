package com.example.eventplanner.fragments.user;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.eventplanner.R;
import com.example.eventplanner.databinding.ActivityUserProfileBinding;
import com.example.eventplanner.databinding.FragmentFavouritesBinding;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserProfileFragment extends Fragment {

    private FragmentUserProfileBinding binding;
    private EditText name;
    private EditText lastName;
    private EditText address;
    private EditText email;
    private EditText phone;
    private PersonRepository personRepository;
    private UserRepository userRepository;
    private String personId;
    private Person person;
    private String userId;
    private String userEmail;
    private Role userRole;

    public UserProfileFragment() {
        // Required empty public constructor
    }


    public static UserProfileFragment newInstance() {
        UserProfileFragment fragment = new UserProfileFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        userId = PreferencesManager.getLoggedUserId(getContext());
        userRole = PreferencesManager.getLoggedUserRole(getContext());

        binding = FragmentUserProfileBinding.inflate(inflater, container, false);
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

        Button editButton = binding.editButton;
        editButton.setOnClickListener(v -> {
            if(person !=null) {
                Log.i("ShopApp", "Clicked Edit Button for: " + person.getName() +
                        ", id: " + person.getId());
                Intent intent = new Intent(getContext(), EditProfileActivity.class);
                intent.putExtra("person", person.getUserId());
                getContext().startActivity(intent);
            }
        });


        return root;
    }
}