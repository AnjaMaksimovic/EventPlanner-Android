package com.example.eventplanner.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.eventplanner.R;
import com.example.eventplanner.activities.user.UserProfileActivity;
import com.example.eventplanner.model.Service;
import com.example.eventplanner.model.User;
import com.example.eventplanner.model.UserReport;
import com.example.eventplanner.model.enums.NotificationStatus;
import com.example.eventplanner.model.enums.ReportStatus;
import com.example.eventplanner.model.enums.Role;
import com.example.eventplanner.model.reservations.Reservation;
import com.example.eventplanner.model.reservations.ReservationStatus;
import com.example.eventplanner.model.system.Notification;
import com.example.eventplanner.repositories.ReservationRepository;
import com.example.eventplanner.repositories.ServiceRepository;
import com.example.eventplanner.repositories.UserReportRepository;
import com.example.eventplanner.repositories.UserRepository;
import com.example.eventplanner.repositories.interfaces.IReservationRepository;
import com.example.eventplanner.repositories.interfaces.IUserRepository;
import com.example.eventplanner.repositories.system.NotificationRepository;
import com.example.eventplanner.settings.PreferencesManager;

import java.util.ArrayList;

public class UserReportListAdapter extends ArrayAdapter<UserReport> {
    private ArrayList<UserReport> aReports;
    private String userEmail;
    private UserReportRepository userReportRepository;
    private UserRepository userRepository;
    private NotificationRepository notificationRepository;
    private ReservationRepository reservationRepository;
    private ServiceRepository serviceRepository;
    private String userId;

    private User reportedUser;

    public UserReportListAdapter(Context context, ArrayList<UserReport> items){
        super(context, R.layout.user_report_card, items);
        aReports = items;
    }

    @Override
    public int getCount() {
        return aReports.size();
    }


    @Nullable
    @Override
    public UserReport getItem(int position) {
        return aReports.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        UserReport report = getItem(position);
        userRepository = new UserRepository();
        userReportRepository = new UserReportRepository();
        reservationRepository = new ReservationRepository();
        notificationRepository = new NotificationRepository();
        serviceRepository = new ServiceRepository();
        userId = PreferencesManager.getLoggedUserId(getContext());
        userEmail = PreferencesManager.getLoggedUserEmail(getContext());

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_report_card,
                    parent, false);
        }
        TextView userRepEmail = convertView.findViewById(R.id.user_rep_email);
        TextView reporteeEmail = convertView.findViewById(R.id.reportee_email);
        TextView reportDate = convertView.findViewById(R.id.report_date);
        TextView reportingReason = convertView.findViewById(R.id.reporting_reason);
        TextView reportStatus = convertView.findViewById(R.id.report_status);
        Button userInfoBtn = convertView.findViewById(R.id.user_info_btn);
        Button reporteeInfoBtn = convertView.findViewById(R.id.reportee_info_btn);
        Button acceptBtn = convertView.findViewById(R.id.accept_btn);
        Button rejectBtn = convertView.findViewById(R.id.reject_btn);


        if(report != null){
            userRepEmail.setText(report.getUserEmail());
            reporteeEmail.setText(report.getReporteeEmail());
            reportDate.setText(report.getReportDate());
            reportingReason.setText(report.getReportingReason());
            reportStatus.setText(report.getStatus().toString());
        }

        userInfoBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), UserProfileActivity.class);
            intent.putExtra("personId", report.getUserId());
            getContext().startActivity(intent);
        });

        reporteeInfoBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), UserProfileActivity.class);
            intent.putExtra("personId", report.getReporteeId());
            getContext().startActivity(intent);
        });

        rejectBtn.setOnClickListener( v -> {
            showRejectReportDialog(report);
        });

        acceptBtn.setOnClickListener(v -> {
            report.setStatus(ReportStatus.approved);
            userReportRepository.updateUserReport(report);
            userRepository.BlockUser(report.getUserId(), new IUserRepository.BlockUserCallback() {
                @Override
                public void onBlockSuccess() {
                    userRepository.GetByEmail(report.getUserEmail(), new IUserRepository.GetUsersCallback() {
                        @Override
                        public void OnGetUsers(ArrayList<User> users) {
                            IUserRepository.GetUsersCallback.super.OnGetUsers(users);
                            if(users.isEmpty()) return;
                            reportedUser = users.get(0);
                            if(reportedUser.getRole().equals(Role.organizator)){
                                processReservationsForOrganizer(reportedUser);
                            } else if (reportedUser.getRole().equals(Role.owner)){
                                processReservationsForOwner(reportedUser);
                            }
                        }
                    });

                    Toast.makeText(getContext(), "User report accepted!\n User blocked successfully", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onBlockFailure(Exception e) {
                    Toast.makeText(getContext(), "Failed to block user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("UserProfileActivity", "Error blocking user", e);
                }
            });

        });

        return convertView;
    }

    private void processReservationsForOrganizer(User reportedUser) {
        reservationRepository.GetAllByOrganizator(reportedUser.getId(), new IReservationRepository.ReservationsCallback() {
            @Override
            public void OnReservationsGet(ArrayList<Reservation> reservationList) {
                IReservationRepository.ReservationsCallback.super.OnReservationsGet(reservationList);
                if (reservationList != null && !reservationList.isEmpty()) {
                    for (Reservation reservation : reservationList) {
                        if (reservation.getStatus().equals(ReservationStatus.newReservation) || reservation.getStatus().equals(ReservationStatus.accepted)) {
                            reservationRepository.Update(reservation.getId(), ReservationStatus.canceledByAdmin, new IReservationRepository.ReservationsCallback() {
                                @Override
                                public void OnReservationsGet(ArrayList<Reservation> reservationList) {
                                    IReservationRepository.ReservationsCallback.super.OnReservationsGet(reservationList);

                                    String employeeId = reservation.getItem().getEmployeeId();
                                    Notification notification = new Notification();
                                    notification.setReceiverId(employeeId);
                                    notification.setSenderId(userId);
                                    notification.setText("Reservation created by " + reservation.getOrganizatorName() + " " + reservation.getOrganizatorLastname() + " is canceled by admin.");
                                    notification.setTitle("Reservation is canceled");
                                    notification.setSenderEmail(userEmail);
                                    notification.setStatus(NotificationStatus.unread);
                                    notificationRepository.Create(notification);
                                }
                            });
                        }
                    }
                }
            }
        });
    }

    private void processReservationsForOwner(User reportedUser) {
        reservationRepository.GetAllByOwner(reportedUser.getId(), new IReservationRepository.ReservationsCallback() {
            @Override
            public void OnReservationsGet(ArrayList<Reservation> reservationList) {
                IReservationRepository.ReservationsCallback.super.OnReservationsGet(reservationList);
                if (reservationList != null && !reservationList.isEmpty()) {
                    for (Reservation reservation : reservationList) {
                        if (reservation.getStatus().equals(ReservationStatus.newReservation) || reservation.getStatus().equals(ReservationStatus.accepted)) {
                            reservationRepository.Update(reservation.getId(), ReservationStatus.canceledByAdmin, new IReservationRepository.ReservationsCallback() {
                                @Override
                                public void OnReservationsGet(ArrayList<Reservation> reservationList) {
                                    IReservationRepository.ReservationsCallback.super.OnReservationsGet(reservationList);

                                    String organizatorId = reservation.getOrganizatorId();
                                    Notification notification = new Notification();
                                    notification.setReceiverId(organizatorId);
                                    notification.setSenderId(userId);
                                    notification.setText("Reservation created by " + reservation.getOrganizatorName() + " " + reservation.getOrganizatorLastname() + " is canceled by admin.");
                                    notification.setTitle("Reservation is canceled");
                                    notification.setSenderEmail(userEmail);
                                    notification.setStatus(NotificationStatus.unread);
                                    notificationRepository.Create(notification);
                                }
                            });
                        }
                    }
                }
            }
        });
    }
    private void showRejectReportDialog(UserReport report) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_reject_report, null);

        final EditText rejectingReason = dialogView.findViewById(R.id.reject_reason);
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
                report.setStatus(ReportStatus.rejected);
                userReportRepository.updateUserReport(report);

                Notification notification = new Notification();
                notification.setReceiverId(report.getReporteeId());
                notification.setSenderId(userId);
                notification.setText("User report for " + report.getUserEmail() + " is rejected.\nReason of rejecting:\n" + rejectingReason.getText().toString());
                notification.setTitle("Rejected user report");
                notification.setSenderEmail(userEmail);
                notification.setStatus(NotificationStatus.unread);
                notificationRepository.Create(notification);

                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
