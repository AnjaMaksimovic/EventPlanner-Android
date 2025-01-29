package com.example.eventplanner.fragments.registration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventplanner.R;
import com.example.eventplanner.databinding.FragmentOrganizerRegistrationBinding;
import com.example.eventplanner.model.Person;
import com.example.eventplanner.model.User;
import com.example.eventplanner.model.enums.Role;
import com.example.eventplanner.repositories.PersonRepository;
import com.example.eventplanner.repositories.UserRepository;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrganizerRegistration#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrganizerRegistration extends Fragment {

    private FragmentOrganizerRegistrationBinding binding;
    private UserRepository userRepository;
    private PersonRepository personRepository;
    public OrganizerRegistration() {
    }

    public static OrganizerRegistration newInstance() {
        OrganizerRegistration fragment = new OrganizerRegistration();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentOrganizerRegistrationBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        userRepository = new UserRepository();
        personRepository = new PersonRepository();
        binding.fabAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
                    startActivityForResult(intent, 1);
                } else {
                    Toast.makeText(requireContext(), "No app can handle this action", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.registerBtnOD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateForm()) {
                    // If form is valid, proceed with registration
                    RegisterEmployee(); // Pozivamo metod za registrovanje korisnika
                } else {
                    // If form is not valid, display error message
                    Toast.makeText(requireContext(), "Please fill in all required fields", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }
    private boolean validateForm() {
        boolean valid = true;

        // Check if email is entered
        String email = binding.emailAddressFieldOD.getText().toString();
        if (TextUtils.isEmpty(email)) {
            binding.emailAddressFieldOD.setError("Enter email");
            valid = false;
        } else {
            binding.emailAddressFieldOD.setError(null);
        }

        // Check if password is entered
        String password = binding.passwordFieldOD.getText().toString();
        if (TextUtils.isEmpty(password)) {
            binding.passwordFieldOD.setError("Enter password");
            valid = false;
        } else {
            binding.passwordFieldOD.setError(null);
        }

        // Check if repeat password is entered and matches the first password
        String repeatPassword = binding.repeatFieldOD.getText().toString();
        if (TextUtils.isEmpty(repeatPassword)) {
            binding.repeatFieldOD.setError("Repeat password");
            valid = false;
        } else if (!repeatPassword.equals(password)) {
            binding.repeatFieldOD.setError("Passwords do not match");
            valid = false;
        } else {
            binding.repeatFieldOD.setError(null);
        }

        // Check if name and last name are entered
        String name = binding.nameFieldOD.getText().toString();
        if (TextUtils.isEmpty(name)) {
            binding.nameFieldOD.setError("Enter name");
            valid = false;
        } else {
            binding.nameFieldOD.setError(null);
        }

        String lastName = binding.lastnameFieldOD.getText().toString();
        if (TextUtils.isEmpty(lastName)) {
            binding.lastnameFieldOD.setError("Enter last name");
            valid = false;
        } else {
            binding.lastnameFieldOD.setError(null);
        }

        // Check if address and phone number are entered
        String address = binding.addressFieldOD.getText().toString();
        if (TextUtils.isEmpty(address)) {
            binding.addressFieldOD.setError("Enter address");
            valid = false;
        } else {
            binding.addressFieldOD.setError(null);
        }

        String phone = binding.phoneFieldOD.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            binding.phoneFieldOD.setError("Enter phone number");
            valid = false;
        } else {
            binding.phoneFieldOD.setError(null);
        }

        return valid;
    }

    public void RegisterEmployee(){
        Log.d("OrganizerRegistration", "RegisterEmployee() called");

        User user = new User();
        if(!SetUser(user)) {
            Log.d("OrganizerRegistration", "SetUser() returned false");
            return;
        }

        Person person = new Person();
        person.setUserId(user.getId());
        if(!SetPerson(person)) {
            Log.d("OrganizerRegistration", "SetPerson() returned false");
            return;
        }

        Log.d("OrganizerRegistration", "User: " + user.toString());
        Log.d("OrganizerRegistration", "Person: " + person.toString());

        //TODO: Get all values from View
        userRepository.Create(user);
        personRepository.Create(person);

        //TODO: Send activation link
    }

    private boolean SetUser(User user){
        Log.d("OrganizerRegistration", "SetUser() called");

        String password = binding.passwordFieldOD.getText().toString();
        String repeatedPassword = binding.repeatFieldOD.getText().toString();
        String email = binding.emailAddressFieldOD.getText().toString();
        Log.d("lozinke", password + repeatedPassword +email);
        if(!password.equals(repeatedPassword)){
            Toast.makeText(getContext(), "Passwords should be same!", Toast.LENGTH_SHORT).show();
            return false;
        }
        user.setEmail(email);
        user.setPassword(password);
        user.setActive(false);
        user.setRole(Role.organizator);

        Log.d("OrganizerRegistration", "User: " + user.toString());

        return true;
    }

    private boolean SetPerson(Person person){
        Log.d("OrganizerRegistration", "SetPerson() called");

        //TODO: Do some validations
        EditText name = binding.nameFieldOD;
        EditText lastname = binding.lastnameFieldOD;
        EditText email = binding.emailAddressFieldOD;
        EditText address = binding.addressFieldOD;
        EditText phoneNumber = binding.phoneFieldOD;
        String photoPath = ""; //TODO: Get from view

        person.setName(name.getText().toString());
        person.setLastname(lastname.getText().toString());
        person.setEmail(email.getText().toString());
        person.setAddress(address.getText().toString());
        person.setPhoneNumber(phoneNumber.getText().toString());
        person.setPhotoPath(photoPath);

        Log.d("OrganizerRegistration", "Person: " + person.toString());

        return true;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("ShopApp", "FirstFragment onAttach()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("ShopApp", "FirstFragment onDestroyView()");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("ShopApp", "FirstFragment onDetach()");
    }
}