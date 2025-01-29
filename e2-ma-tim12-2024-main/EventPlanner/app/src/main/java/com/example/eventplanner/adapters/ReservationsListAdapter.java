package com.example.eventplanner.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.eventplanner.R;
import com.example.eventplanner.activities.user.UserProfileActivity;
import com.example.eventplanner.model.enums.Role;
import com.example.eventplanner.model.reservations.Reservation;
import com.example.eventplanner.model.reservations.ReservationItem;
import com.example.eventplanner.model.reservations.ReservationStatus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ReservationsListAdapter extends ArrayAdapter<Reservation> {

    private Role aUserRole;
    private ArrayList<Reservation> aReservations;
    private ReservationsListAdapter.OnReservationClickListener mListener;

    public interface  OnReservationClickListener{
        void OnCancelClick(Reservation reservation);
        void OnAcceptClick(Reservation reservation);
        void OnRateClick(Reservation reservation);
    }
    public ReservationsListAdapter(@NonNull Context context, ArrayList<Reservation> reservations, Role userRole, OnReservationClickListener listener) {
        super(context, R.layout.reservations_card, reservations);
        aReservations = reservations;
        aUserRole = userRole;
        mListener = listener;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Nullable
    @Override
    public Reservation getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       Reservation reservation = getItem(position);
       if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.reservations_card,
                    parent, false);
       }
        TextView reservationId = convertView.findViewById(R.id.reservation_id);
        TextView creationDate = convertView.findViewById(R.id.reservation_creation_date);
        TextView status = convertView.findViewById(R.id.reservation_status);
        TextView organizer = convertView.findViewById(R.id.reservation_organizator_info);
        TextView employee = convertView.findViewById(R.id.reservation_employee_info);
        TextView service = convertView.findViewById(R.id.reservation_service);
        TextView inPackage = convertView.findViewById(R.id.reservation_in_package);
        Button acceptBtn  = convertView.findViewById(R.id.reservation_accept_btn);
        Button cancelBtn  = convertView.findViewById(R.id.reservation_cancel_btn);
        Button rateBtn  = convertView.findViewById(R.id.reservation_rate_btn);
        Button userInfoBtn = convertView.findViewById(R.id.user_info);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        if(reservation != null){
            if(aUserRole.equals(Role.organizator)){
                acceptBtn.setVisibility(View.GONE);
            }
            else {
                rateBtn.setVisibility(View.GONE);
            }
            if(!aUserRole.equals(Role.owner)){
                userInfoBtn.setVisibility(View.GONE);
            }
           ReservationItem item = reservation.getItem();
            reservationId.setText(reservation.getId());
            creationDate.setText(dateFormat.format(reservation.getReservationDate()));
            status.setText(reservation.getStatus().toString());
            organizer.setText(new StringBuilder().append(reservation.getOrganizatorName()).append(" ").append(reservation.getOrganizatorLastname()).toString());
            employee.setText(new StringBuilder().append(item.getEmployeeName()).append(" ").append(item.getEmployeeLastname()).toString());
            service.setText(item.getServiceName());
            inPackage.setText(reservation.isPackage() ? reservation.getPackageId() : "/");


            //TODO: Set based on logged user
            acceptBtn.setEnabled(reservation.getStatus().equals(ReservationStatus.newReservation));
            acceptBtn.setOnClickListener(v -> {
                Log.i("ReservationsListAdapter", "Reservation accepted");
                if(mListener != null){
                    mListener.OnAcceptClick(reservation);
                }
            });
           cancelBtn.setEnabled(reservation.getStatus().equals(ReservationStatus.newReservation) || reservation.getStatus().equals(ReservationStatus.accepted));
           cancelBtn.setOnClickListener(v -> {
               Log.i("ReservationsListAdapter", "Reservation canceled");
               if(mListener != null){
                   mListener.OnCancelClick(reservation);
               }
           });
           rateBtn.setEnabled((reservation.getStatus().equals(ReservationStatus.canceledByAdmin) || reservation.getStatus().equals(ReservationStatus.canceledByOrganizator) ||
                   reservation.getStatus().equals(ReservationStatus.canceledByPup) || reservation.getStatus().equals(ReservationStatus.realized)));
           rateBtn.setOnClickListener(v -> {
               Log.i("ReservationsListAdapter", "Reservation rating of company");
               if(mListener != null){
                   mListener.OnRateClick(reservation);
               }
           });
           userInfoBtn.setOnClickListener(v -> {
               Intent intent = new Intent(getContext(), UserProfileActivity.class);
               intent.putExtra("personId", reservation.getOrganizatorId());
               getContext().startActivity(intent);
           });
        }
       return convertView;
    }
}