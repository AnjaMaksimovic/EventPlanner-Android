package com.example.eventplanner.fragments.registration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eventplanner.R;
import com.example.eventplanner.databinding.FragmentOrganizerRegistrationBinding;
import com.example.eventplanner.databinding.FragmentOwnerRegistrationBinding;
import com.example.eventplanner.model.Company;
import com.example.eventplanner.model.Person;
import com.example.eventplanner.model.User;
import com.example.eventplanner.model.enums.Role;
import com.example.eventplanner.repositories.CompanyRepository;
import com.example.eventplanner.repositories.PersonRepository;
import com.example.eventplanner.repositories.UserRepository;
import com.example.eventplanner.repositories.interfaces.ICompanyRepository;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OwnerRegistration#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OwnerRegistration extends Fragment {

    private FragmentOwnerRegistrationBinding binding;
    private UserRepository userRepository;
    private PersonRepository personRepository;
    private CompanyRepository companyRepository;
    public OwnerRegistration() {
    }

    public static OwnerRegistration newInstance() {
        OwnerRegistration fragment = new OwnerRegistration();

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
        //Log.d("ShopApp", "SecondFragment onCreateView()");
        //binding = FragmentOwnerRegistrationBinding.inflate(inflater, container, false);
        //View view = binding.fragment_owner_registration;
        //View view = inflater.inflate(R.layout.fragment_owner_registration, container, false);

        binding = FragmentOwnerRegistrationBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        userRepository = new UserRepository();
        personRepository = new PersonRepository();
        companyRepository = new CompanyRepository();
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

        binding.registerBtnPUPV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateForm()) {
                    // If form is valid, proceed with registration
                    RegisterOwner(); // Pozivamo metod za registrovanje korisnika
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
        String email = binding.emailAddressFieldPUPV.getText().toString();
        if (TextUtils.isEmpty(email)) {
            binding.emailAddressFieldPUPV.setError("Enter email");
            valid = false;
        } else {
            binding.emailAddressFieldPUPV.setError(null);
        }

        // Check if password is entered
        String password = binding.passwordFieldPUPV.getText().toString();
        if (TextUtils.isEmpty(password)) {
            binding.passwordFieldPUPV.setError("Enter password");
            valid = false;
        } else {
            binding.passwordFieldPUPV.setError(null);
        }

        // Check if repeat password is entered and matches the first password
        String repeatPassword = binding.repeatFieldPUPV.getText().toString();
        if (TextUtils.isEmpty(repeatPassword)) {
            binding.repeatFieldPUPV.setError("Repeat password");
            valid = false;
        } else if (!repeatPassword.equals(password)) {
            binding.repeatFieldPUPV.setError("Passwords do not match");
            valid = false;
        } else {
            binding.repeatFieldPUPV.setError(null);
        }

        // Check if name and last name are entered
        String name = binding.nameFieldPUPV.getText().toString();
        if (TextUtils.isEmpty(name)) {
            binding.nameFieldPUPV.setError("Enter name");
            valid = false;
        } else {
            binding.nameFieldPUPV.setError(null);
        }

        String lastName = binding.lastnameFieldPUPV.getText().toString();
        if (TextUtils.isEmpty(lastName)) {
            binding.lastnameFieldPUPV.setError("Enter last name");
            valid = false;
        } else {
            binding.lastnameFieldPUPV.setError(null);
        }

        // Check if address and phone number are entered
        String address = binding.addressFieldPUPV.getText().toString();
        if (TextUtils.isEmpty(address)) {
            binding.addressFieldPUPV.setError("Enter address");
            valid = false;
        } else {
            binding.addressFieldPUPV.setError(null);
        }

        String phone = binding.phoneFieldPUPV.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            binding.phoneFieldPUPV.setError("Enter phone number");
            valid = false;
        } else {
            binding.phoneFieldPUPV.setError(null);
        }

        return valid;
    }


    public void RegisterOwner(){
        Log.d("OrganizerRegistration", "RegisterEmployee() called");

        User user = new User();
        if(!SetUser(user)) {
            Log.d("OrganizerRegistration", "SetUser() returned false");
            return;
        }

        Person person = new Person();
        Company company = new Company();
        person.setUserId(user.getId());
        person.setCompanyId(company.getId());
        if(!SetPerson(person)) {
            Log.d("OrganizerRegistration", "SetPerson() returned false");
            return;
        }

        company.setOwnerId(user.getId());
        company.setOwnerName(person.getName());
        company.setOwnerLastname(person.getLastname());
        company.setOwnerEmail(person.getEmail());
        company.setRequestHandled(false);

        if(!SetCompany(company)) {
            Log.d("OrganizerRegistration", "SetCompany() returned false");
            return;
        }

        Log.d("OrganizerRegistration", "User: " + user.toString());
        Log.d("OrganizerRegistration", "Person: " + person.toString());
        Log.d("OrganizerRegistration", "Company: " + company.toString());

        //TODO: Get all values from View
        userRepository.Create(user);
        personRepository.Create(person);
        companyRepository.Create(company);

        //TODO: Send activation link
    }

    private boolean SetUser(User user){
        Log.d("OrganizerRegistration", "SetUser() called");

        String password = binding.passwordFieldPUPV.getText().toString();
        String repeatedPassword = binding.repeatFieldPUPV.getText().toString();
        String email = binding.emailAddressFieldPUPV.getText().toString();
        Log.d("lozinke", password + repeatedPassword +email);
        if(!password.equals(repeatedPassword)){
            Toast.makeText(getContext(), "Passwords should be same!", Toast.LENGTH_SHORT).show();
            return false;
        }
        user.setEmail(email);
        user.setPassword(password);
        user.setActive(false);
        user.setRole(Role.owner);

        Log.d("OrganizerRegistration", "User: " + user.toString());

        return true;
    }

    private boolean SetPerson(Person person){
        Log.d("OrganizerRegistration", "SetPerson() called");

        //TODO: Do some validations
        EditText name = binding.nameFieldPUPV;
        EditText lastname = binding.lastnameFieldPUPV;
        EditText email = binding.emailAddressFieldPUPV;
        EditText address = binding.addressFieldPUPV;
        EditText phoneNumber = binding.phoneFieldPUPV;
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

    private boolean SetCompany(Company company){
        Log.d("OrganizerRegistration", "SetCompany() called");

        //TODO: Do some validations
        EditText email = binding.companyEmailAddressFieldPUPV;
        EditText name = binding.companyFieldPUPV;
        EditText address = binding.companyAddressFieldPUPV;
        EditText phoneNumber = binding.companyPhoneFieldPUPV;
        EditText description = binding.companyDescriptionFieldPUPV;
        //String photoPath = ""; //TODO: Get from view

        company.setName(name.getText().toString());
        company.setDescrtiption(description.getText().toString());
        company.setEmail(email.getText().toString());
        company.setCompanyAdress(address.getText().toString());
        company.setPhoneNumber(phoneNumber.getText().toString());
        //company.setPhotoPath(photoPath);
        company.setCreateDate(new Date());

        Log.d("OrganizerRegistration", "Company: " + company.toString());

        return true;
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