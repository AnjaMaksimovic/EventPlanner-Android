package com.example.eventplanner.fragments.stakeholders;

import static androidx.core.content.ContextCompat.getDrawable;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eventplanner.LoginFragment;
import com.example.eventplanner.R;
import com.example.eventplanner.databinding.FragmentEmployeeRegistrationBinding;
import com.example.eventplanner.fragments.FragmentTransition;
import com.example.eventplanner.model.Person;
import com.example.eventplanner.model.User;
import com.example.eventplanner.model.WorkSchedule;
import com.example.eventplanner.model.WorkingHours;
import com.example.eventplanner.model.enums.Day;
import com.example.eventplanner.model.enums.Role;
import com.example.eventplanner.repositories.PersonRepository;
import com.example.eventplanner.repositories.UserRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.ktx.Firebase;

import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

public class EmployeeRegistrationFragment extends Fragment {

    private UserRepository userRepository;
    private PersonRepository personRepository;
    private FragmentEmployeeRegistrationBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Dialog workingHoursDialog;
    Button confirmWorkingHoursBtn;
    Button cancelWorkingHoursBtn;

    public EmployeeRegistrationFragment() {
    }

    // TODO: Rename and change types and number of parameters
    public static EmployeeRegistrationFragment newInstance(String param1, String param2) {
        EmployeeRegistrationFragment fragment = new EmployeeRegistrationFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        userRepository = new UserRepository();
        personRepository = new PersonRepository();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        workingHoursDialog = new Dialog(getContext());
        workingHoursDialog.setContentView(R.layout.working_hours_dialog);
        workingHoursDialog.setCancelable(false);
        workingHoursDialog.getWindow().setBackgroundDrawable(getDrawable(getActivity(), R.drawable.working_hours_bg));

        cancelWorkingHoursBtn = workingHoursDialog.findViewById(R.id.cancel_wh_btn);
        confirmWorkingHoursBtn = workingHoursDialog.findViewById(R.id.confirm_wh_btn);
        cancelWorkingHoursBtn.setOnClickListener(v -> {
            workingHoursDialog.dismiss();
        });
        confirmWorkingHoursBtn.setOnClickListener(v -> {
            workingHoursDialog.dismiss();
        });

        binding = FragmentEmployeeRegistrationBinding.inflate(inflater, container, false);

        binding.registrationInclude.registerBtn.setOnClickListener(v -> {
            RegisterEmployee();
        });

        binding.registrationInclude.cancelRegistrationBtn.setOnClickListener(v -> {
            FragmentTransition.to(EmployeesFragment.newInstance("", ""), getActivity(), false, R.id.fragment_nav_content_main);
        });

        binding.registrationInclude.wcDialogBtn.setOnClickListener(v -> {
            workingHoursDialog.show();
        });

        return binding.getRoot();
    }

    public void RegisterEmployee() {
        User user = SetUser();
        Person person = SetPerson(user.getId());

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        Log.d("Employee_reg_msg", "Started registration");

        firebaseAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
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
                                                    Log.d("Employee_reg_msg", "Verification email sent.");
                                                } else {
                                                    Log.e("Employee_reg_msg", "Failed to send verification email.", task.getException());
                                                }
                                            }
                                        });

                                userRepository.Create(user);
                                personRepository.Create(person);
                                Log.d("Employee_reg_msg", "User and Person created in repositories.");
                                Toast.makeText(EmployeeRegistrationFragment.this.getContext(), "Employee registration successful", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e("Employee_reg_msg", "FirebaseUser is null after registration.");
                            }
                        } else {
                            // Registration failed
                            Log.e("Employee_reg_msg", "User registration failed.", task.getException());
                            Toast.makeText(EmployeeRegistrationFragment.this.getContext(), "Employee registration unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private User SetUser(){
        String password = binding.registrationInclude.passwordField.getText().toString();
        String repeatedPassword = binding.registrationInclude.repeatField.getText().toString();
        String email = binding.registrationInclude.emailAddressField.getText().toString();
        if(!password.equals(repeatedPassword)){
            Toast.makeText(getContext(), "Passwords should be same!", Toast.LENGTH_SHORT).show();
            return null;
        }
        User user = new User(email, password, Role.employee, false);
        return user;
    }

    private Person SetPerson(String userId){
        String name = binding.registrationInclude.nameField.getText().toString();
        String lastname = binding.registrationInclude.lastnameField.getText().toString();
        String email = binding.registrationInclude.emailAddressField.getText().toString();
        String address = binding.registrationInclude.addressField.getText().toString();
        String phoneNumber = binding.registrationInclude.phoneField.getText().toString();
        String photoPath = ""; //TODO: Get from view
        ArrayList<WorkSchedule> workSchedule = SetWorkSchedule();

        //TODO: Access CompanyId when, Repo for Companies is created
        Person person = new Person(userId, name, lastname, email, address, phoneNumber, photoPath, workSchedule, "Company_1");
        return person;
    }

    private ArrayList<WorkSchedule> SetWorkSchedule(){
        ArrayList<WorkSchedule> workSchedules = new ArrayList<>();
        ArrayList<WorkingHours> workingHours = new ArrayList<>();

        WorkSchedule workSchedule = new WorkSchedule();
        workSchedule.setStart(new Date());
        workSchedule.setEnd(null);

        ArrayList<String> times = new ArrayList<>();
        for(int i = 0; i < Day.values().length; i++){
            times = ParseTimeForDay(i);
            WorkingHours workingHour = new WorkingHours(times.get(0), times.get(1), Day.values()[i]);
            workingHours.add(workingHour);
        }
        workSchedule.setWorkingHours(workingHours);
        workSchedules.add(workSchedule);
        return workSchedules;
    }

    private ArrayList<String> ParseTimeForDay(int i) {
        ArrayList<String> times = new ArrayList<>();
        switch (i){
            case 0:{
                EditText from = workingHoursDialog.findViewById(R.id.monday_from);
                EditText to = workingHoursDialog.findViewById(R.id.monday_to);
                AddTimes(from, to, times);
                break;
            }
            case 1:{
                EditText from = workingHoursDialog.findViewById(R.id.tuesday_from);
                EditText to = workingHoursDialog.findViewById(R.id.tuesday_to);
                AddTimes(from, to, times);
                break;
            }
            case 2:{
                EditText from = workingHoursDialog.findViewById(R.id.wednesday_from);
                EditText to = workingHoursDialog.findViewById(R.id.wednesday_to);
                AddTimes(from, to, times);
                break;
            }
            case 3:{
                EditText from = workingHoursDialog.findViewById(R.id.thursday_from);
                EditText to = workingHoursDialog.findViewById(R.id.thursday_to);
                AddTimes(from, to, times);
                break;
            }
            case 4:{
                EditText from = workingHoursDialog.findViewById(R.id.friday_from);
                EditText to = workingHoursDialog.findViewById(R.id.friday_to);
                AddTimes(from, to, times);
                break;
            }
            case 5:{
                EditText from = workingHoursDialog.findViewById(R.id.saturday_from);
                EditText to = workingHoursDialog.findViewById(R.id.saturday_to);
                AddTimes(from, to, times);
                break;
            }
            default:{
                EditText from = workingHoursDialog.findViewById(R.id.sunday_from);
                EditText to = workingHoursDialog.findViewById(R.id.sunday_to);
                AddTimes(from, to, times);
                break;
            }
        }
        return times;
    }

    private void AddTimes(EditText from, EditText to, ArrayList<String> times) {
        String fromString = from.getText().toString();
        String toString = to.getText().toString();
        if(fromString.isEmpty() || toString.isEmpty()){
            times.add(null);
            times.add(null);
            return;
        }
        times.add(fromString);
        times.add(toString);
    }
}