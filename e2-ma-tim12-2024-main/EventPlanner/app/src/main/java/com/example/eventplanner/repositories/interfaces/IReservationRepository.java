package com.example.eventplanner.repositories.interfaces;

import com.example.eventplanner.model.reservations.Reservation;
import com.example.eventplanner.model.reservations.ReservationStatus;
import com.example.eventplanner.repositories.reviews.interfaces.IReviewReportRepository;

import java.util.ArrayList;

public interface IReservationRepository {
    public void GetAll(ReservationsCallback callback);
    public void GetAllByEmployee(String employeeId, ReservationsCallback callback);
    public void GetAllByOrganizator(String organizatorId, ReservationsCallback callback);
    public void GetAllByOwner(String ownerId, ReservationsCallback callback);
    public void Update(String reservationId, ReservationStatus newStatus, ReservationsCallback callback);

    void Create(Reservation reservation);

    public interface ReservationsCallback{
        default void OnReservationsGet(ArrayList<Reservation> reservationList){}
        default void OnResult(boolean succeeded){}
    }
}