package com.example.eventplanner.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.eventplanner.R;
import com.example.eventplanner.model.Company;
import com.example.eventplanner.model.CompanyReport;
import com.example.eventplanner.model.User;
import com.example.eventplanner.model.enums.NotificationStatus;
import com.example.eventplanner.model.system.Notification;
import com.example.eventplanner.repositories.CompanyReportRepository;
import com.example.eventplanner.repositories.UserRepository;
import com.example.eventplanner.repositories.interfaces.IUserRepository;
import com.example.eventplanner.repositories.system.NotificationRepository;
import com.example.eventplanner.settings.PreferencesManager;

import java.util.ArrayList;
import java.util.List;

public class CompanyListAdapter  extends ArrayAdapter<Company> {
    private ArrayList<Company> aCompanies;
    private CompanyReportRepository companyRepo;
    private String userEmail;
    private UserRepository userRepository;
    private NotificationRepository notificationRepository;
    private List<User> admins = new ArrayList<>();
    private String userId;

    public CompanyListAdapter(Context context, ArrayList<Company> items){
        super(context, R.layout.company_item, items);
        aCompanies = items;
    }

    @Override
    public int getCount() {
        return aCompanies.size();
    }


    @Nullable
    @Override
    public Company getItem(int position) {
        return aCompanies.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Company company = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.company_item,
                    parent, false);
        }
        TextView companyName = convertView.findViewById(R.id.name);
        TextView companyAddress = convertView.findViewById(R.id.address);
        TextView ownerName = convertView.findViewById(R.id.ownerName);
        TextView ownerLastName = convertView.findViewById(R.id.ownerLastName);
        Button reportButton = convertView.findViewById(R.id.reportButton);


        if(company != null){
            companyName.setText(company.getName());
            companyAddress.setText("" + company.getCompanyAdress());
            ownerName.setText("" + company.getOwnerName());
            ownerLastName.setText("" + company.getOwnerLastname());
        }

        reportButton.setOnClickListener(v -> {
            showAddSubcategoryDialog(company);
        });

        return convertView;
    }

    private void showAddSubcategoryDialog(Company company) {
        userRepository = new UserRepository();
        notificationRepository = new NotificationRepository();
        userId = PreferencesManager.getLoggedUserId(getContext());
        userEmail = PreferencesManager.getLoggedUserEmail(getContext());
        userRepository.getAllAdmins( new IUserRepository.GetUsersCallback() {
            @Override
            public void OnGetUsers(ArrayList<User> users) {
                IUserRepository.GetUsersCallback.super.OnGetUsers(users);
                if(users != null) {
                    admins = users;
                }
            }
        });
        companyRepo = new CompanyReportRepository();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_report_company, null);

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
                CompanyReport report = new CompanyReport();
                report.setCompanyId(company.getId());
                report.setReportingReason(reportingReason.getText().toString());
                report.setReporteeEmail(userEmail);
                companyRepo.createCompanyReport(report);

                for(User admin : admins){
                    Notification notification = new Notification();
                    notification.setReceiverId(admin.getId());
                    notification.setSenderId(userId);
                    notification.setText("Company " + company.getName() + " is reported by " + report.getReporteeEmail());
                    notification.setTitle("Company reporting notification");
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
