package com.example.eventplanner.activities.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventplanner.R;
import com.example.eventplanner.databinding.ActivityUserProfileBinding;
import com.example.eventplanner.model.Person;
import com.example.eventplanner.model.User;
import com.example.eventplanner.model.UserReport;
import com.example.eventplanner.model.enums.NotificationStatus;
import com.example.eventplanner.model.enums.ReportStatus;
import com.example.eventplanner.model.enums.Role;
import com.example.eventplanner.model.system.Notification;
import com.example.eventplanner.repositories.PersonRepository;
import com.example.eventplanner.repositories.UserReportRepository;
import com.example.eventplanner.repositories.UserRepository;
import com.example.eventplanner.repositories.interfaces.IPersonRepository;
import com.example.eventplanner.repositories.interfaces.IUserRepository;
import com.example.eventplanner.repositories.system.NotificationRepository;
import com.example.eventplanner.settings.PreferencesManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserProfileActivity extends AppCompatActivity {

    private ActivityUserProfileBinding binding;
    private EditText name;
    private EditText lastName;
    private EditText address;
    private EditText email;
    private EditText phone;
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
        userRole = PreferencesManager.getLoggedUserRole(this);

        binding = ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        personRepository = new PersonRepository();


        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("personId")){
            personId = intent.getStringExtra("personId");


            personRepository.getPersonByUserId(personId,  new IPersonRepository.GetPersonsCallback() {
                @Override
                public void OnGetPeople(ArrayList<Person> people) {
                    if (people != null && !people.isEmpty()) {
                        person = people.get(0);
                        if (person != null) {
                            name = binding.userProfileFormInclude.userName;
                            name.setText(String.valueOf(person.getName()));
                            lastName = binding.userProfileFormInclude.userLastname;
                            lastName.setText(String.valueOf(person.getLastname()));
                            email = binding.userProfileFormInclude.userEmail;
                            email.setText(String.valueOf(person.getEmail()));
                            address = binding.userProfileFormInclude.userAddress;
                            address.setText(String.valueOf(person.getAddress()));
                            phone = binding.userProfileFormInclude.userPhone;
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

            Button reportButton = binding.userProfileFormInclude.reportButton;

            if(!userRole.equals(Role.owner)){
                reportButton.setVisibility(View.INVISIBLE);
            }

            reportButton.setOnClickListener(v -> {
                showAddSubcategoryDialog(person);
            });
        }

        ImageButton backBtn = binding.userProfileFormInclude.btnBack;
        backBtn.setOnClickListener(v -> {
            finish();
        });
    }

    private void showAddSubcategoryDialog(Person person) {
        userRepository = new UserRepository();
        notificationRepository = new NotificationRepository();
        userId = PreferencesManager.getLoggedUserId(this);
        userEmail = PreferencesManager.getLoggedUserEmail(this);
        userRepository.getAllAdmins( new IUserRepository.GetUsersCallback() {
            @Override
            public void OnGetUsers(ArrayList<User> users) {
                IUserRepository.GetUsersCallback.super.OnGetUsers(users);
                if(users != null) {
                    admins = users;
                }
            }
        });
        userReportRepository = new UserReportRepository();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_report_company, null);

        final EditText reportingReason = dialogView.findViewById(R.id.report_reason);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        Button btnOk = dialogView.findViewById(R.id.btnOk);

        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserReport report = new UserReport();
                report.setUserId(person.getUserId());
                report.setReportingReason(reportingReason.getText().toString());
                report.setReporteeEmail(userEmail);
                report.setUserEmail(person.getEmail());
                report.setReporteeId(userId);
                report.setStatus(ReportStatus.reported);
                LocalDateTime currentTime = LocalDateTime.now();
                report.setReportDate(currentTime.toString());
                userReportRepository.createUserReport(report);

                for(User admin : admins){
                    Notification notification = new Notification();
                    notification.setReceiverId(admin.getId());
                    notification.setSenderId(userId);
                    notification.setText("User " + person.getName() + " is reported by " + report.getReporteeEmail());
                    notification.setTitle("User reporting notification");
                    notification.setSenderEmail(userEmail);
                    notification.setStatus(NotificationStatus.unread);
                    notificationRepository.Create(notification);
                }

                dialog.dismiss();
            }
        });

        dialog.show();
    }
}