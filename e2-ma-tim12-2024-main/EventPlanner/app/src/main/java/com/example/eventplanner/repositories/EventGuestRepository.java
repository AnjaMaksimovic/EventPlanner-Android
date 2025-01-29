package com.example.eventplanner.repositories;
import android.util.Log;
import androidx.annotation.NonNull;

import com.example.eventplanner.model.Event;
import com.example.eventplanner.model.EventGuest;
import com.example.eventplanner.model.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class EventGuestRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void Create(EventGuest eventGuest){
        eventGuest.setId(generateId());
        db.collection("eventGuests")
                .document(eventGuest.getId())
                .set(eventGuest)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("REZ_DB", "DocumentSnapshot added with ID: " + eventGuest.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("REZ_DB", "Error adding document", e);
                    }
                });
    }

    public void updateEventGuest(EventGuest eventGuest) {
        db.collection("eventGuests")
                .document(eventGuest.getId())
                .set(eventGuest)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("REZ_DB", "DocumentSnapshot updated with ID: " + eventGuest.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("REZ_DB", "Error updating document", e);
                    }
                });
    }

    public void Delete(String id){
        DocumentReference docRef = db.collection("eventGuests").document(id);
        docRef.delete()
                .addOnSuccessListener(aVoid -> Log.d("REZ_DB", "Service successfully deleted"))
                .addOnFailureListener(e -> Log.w("REZ_DB", "Error deleting documents.", e));
    }

    public void GetAllEventGuests(EventGuestRepository.EventGuestFetchCallback callback) {
        ArrayList<EventGuest> eventGuests = new ArrayList<>();

        db.collection("eventGuests")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            EventGuest eventGuest = document.toObject(EventGuest.class);
                            eventGuests.add(eventGuest);
                        }
                        callback.onEventGuestFetch(eventGuests);
                    } else {
                        Log.w("REZ_DB", "Error getting documents.", task.getException());
                        callback.onEventGuestFetch(null);
                    }
                });
    }

    public interface EventGuestFetchCallback {
        default void onEventGuestFetch(ArrayList<EventGuest> eventGuests) {
        }
        default void onResult(boolean contains) {
        }
    }

    public static String generateId() {
        return "event_guest_" + System.currentTimeMillis();
    }
}
