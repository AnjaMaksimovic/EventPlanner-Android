package com.example.eventplanner.fragments.stakeholders;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.eventplanner.R;
import com.example.eventplanner.databinding.FragmentInfoBinding;
import com.example.eventplanner.model.Person;
import com.example.eventplanner.model.User;
import com.example.eventplanner.repositories.UserRepository;
import com.example.eventplanner.repositories.interfaces.IUserRepository;

import java.util.ArrayList;

public class InfoFragment extends Fragment {

    private UserRepository userRepository;
    private FragmentInfoBinding binding;
    private Button activationBtn;
    private static final String ARG_PERSON = "person";
    private Person mPerson;
    private User mUser;

    public InfoFragment() {
        // Required empty public constructor
    }
    public static InfoFragment newInstance(Person person) {
        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PERSON,person);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPerson = getArguments().getParcelable(ARG_PERSON);
        }
        userRepository = new UserRepository();
        userRepository.GetByEmail(mPerson.getEmail(), new IUserRepository.GetUsersCallback() {
            @Override
            public void OnGetUsers(ArrayList<User> users) {
                IUserRepository.GetUsersCallback.super.OnGetUsers(users);
                if(users.isEmpty()) return;
                mUser = users.get(0);
                SetActivationButton();
            }
        });

    }

    private void SetActivationButton() {
        activationBtn = binding.personDetails.activateEmployeeBtn;
        activationBtn.setText(mUser.isActive()?"Deactivate profile":"Activate profile");
        activationBtn.setOnClickListener(v -> {
            if(mUser.isActive()){
                userRepository.Deactivate(mUser.getId(), new IUserRepository.GetUsersCallback() {
                    @Override
                    public void OnGetUsers(ArrayList<User> users) {
                        IUserRepository.GetUsersCallback.super.OnGetUsers(users);
                        if(users.isEmpty()) return;
                        mUser = users.get(0);
                        activationBtn.setText(mUser.isActive()?"Deactivate profile":"Activate profile");
                    }
                });
            }
            else{
                userRepository.Activate(mUser.getId(), new IUserRepository.GetUsersCallback() {
                    @Override
                    public void OnGetUsers(ArrayList<User> users) {
                        IUserRepository.GetUsersCallback.super.OnGetUsers(users);
                        if(users.isEmpty()) return;
                        mUser = users.get(0);
                        activationBtn.setText(mUser.isActive()?"Deactivate profile":"Activate profile");
                    }
                });
                //TODO: Send activation link to user's email
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInfoBinding.inflate(inflater, container, false);
        SetPersonView();
        return binding.getRoot();
    }

    private void SetPersonView() {
        TextView name = binding.personDetails.firstNameTv;
        TextView lastname = binding.personDetails.lastNameTv;
        TextView email = binding.personDetails.emailTv;
        TextView address = binding.personDetails.locationTv;
        TextView phone = binding.personDetails.phoneTv;

        name.setText(mPerson.getName());
        lastname.setText(mPerson.getLastname());
        email.setText(mPerson.getEmail());
        address.setText(mPerson.getAddress());
        phone.setText(mPerson.getPhoneNumber());
    }
}