package com.example.eventplanner.repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.eventplanner.model.reservations.Reservation;
import com.example.eventplanner.model.reservations.ReservationStatus;
import com.example.eventplanner.repositories.interfaces.IReservationRepository;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ReservationRepository implements IReservationRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    public void GetAll(ReservationsCallback callback) {
        ArrayList<Reservation> reservations = new ArrayList<>();
        db.collection("reservations")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Reservation reservation = document.toObject(Reservation.class);
                            reservations.add(reservation);
                        }
                        callback.OnReservationsGet(reservations);
                    } else {
                        Log.w("REZ_DB", "Error getting documents.", task.getException());
                    }
                });
    }
    @Override
    public void GetAllByEmployee(String employeeId, ReservationsCallback callback) {
        ArrayList<Reservation> reservations = new ArrayList<>();
        db.collection("reservations")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Reservation reservation = document.toObject(Reservation.class);
                            if(reservation.getItem().getEmployeeId().equals(employeeId)){
                                reservations.add(reservation);
                            }
                        }
                        callback.OnReservationsGet(reservations);
                    } else {
                        Log.w("REZ_DB", "Error getting documents.", task.getException());
                    }
                });
    }

    @Override
    public void GetAllByOrganizator(String organizatorId, ReservationsCallback callback) {
        ArrayList<Reservation> reservations = new ArrayList<>();
        db.collection("reservations")
                .whereEqualTo("organizatorId", organizatorId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Reservation reservation = document.toObject(Reservation.class);
                            reservations.add(reservation);
                        }
                        callback.OnReservationsGet(reservations);
                    } else {
                        Log.w("REZ_DB", "Error getting documents.", task.getException());
                    }
                });
    }

    @Override
    public void GetAllByOwner(String ownerId, ReservationsCallback callback) {
        ArrayList<Reservation> reservations = new ArrayList<>();
        db.collection("reservations")
                .whereEqualTo("ownerId", ownerId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Reservation reservation = document.toObject(Reservation.class);
                            reservations.add(reservation);
                        }
                        callback.OnReservationsGet(reservations);
                    } else {
                        Log.w("REZ_DB", "Error getting documents.", task.getException());
                    }
                });
    }

    @Override
    public void Update(String reservationId, ReservationStatus newStatus, ReservationsCallback callback) {
        ArrayList<Reservation> reservations = new ArrayList<>();
        db.collection("reservations")
                .whereEqualTo("id", reservationId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for(DocumentSnapshot documentSnapshot: queryDocumentSnapshots.getDocuments()){
                        db.collection("reservations").document(documentSnapshot.getId())
                                .update("status", newStatus)
                                .addOnSuccessListener(aVoid -> {
                                    Reservation reservation = documentSnapshot.toObject(Reservation.class);
                                    reservation.setStatus(newStatus);
                                    reservations.add(reservation);
                                    callback.OnReservationsGet(reservations);
                                    Log.d("REZ_DB", "DocumentSnapshot successfully updated!");
                                })
                                .addOnFailureListener(e -> Log.w("REZ_DB", "Error updating document", e));
                    }
                })
                .addOnFailureListener(e -> Log.w("REZ_DB", "Error getting documents", e));
    }

    @Override
    public void Create(Reservation reservation) {
        db.collection("reservations")
                .add(reservation)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("REZ_DB", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("REZ_DB", "Error adding document", e);
                    }
                });
    }

}