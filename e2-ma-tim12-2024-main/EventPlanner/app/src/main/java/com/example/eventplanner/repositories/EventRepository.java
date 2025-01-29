package com.example.eventplanner.repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.eventplanner.model.Event;
import com.example.eventplanner.model.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class EventRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void create(Event event){
        event.setId(generateId());
        db.collection("events")
                .document(event.getId())
                .set(event)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("REZ_DB", "DocumentSnapshot added with ID: " + event.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("REZ_DB", "Error adding document", e);
                    }
                });
    }

    public void update(Event event) {
        db.collection("events")
                .document(event.getId())
                .set(event)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("REZ_DB", "DocumentSnapshot updated with ID: " + event.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("REZ_DB", "Error updating document", e);
                    }
                });
    }

    public void delete(String id){
        DocumentReference docRef = db.collection("events").document(id);
        docRef.update("deleted", true)
                .addOnSuccessListener(aVoid -> Log.d("REZ_DB", "Service successfully changed"))
                .addOnFailureListener(e -> Log.w("REZ_DB", "Error getting documents.", e));
    }

    public void getAllEvents(EventFetchCallback callback) {
        ArrayList<Event> events = new ArrayList<>();

        db.collection("events")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Event event = document.toObject(Event.class);
                            events.add(event);
                        }
                        callback.onEventFetch(events);
                    } else {
                        Log.w("REZ_DB", "Error getting documents.", task.getException());
                        callback.onEventFetch(null);
                    }
                });
    }

    public void getEventsByOrganiserId(String userId, EventFetchCallback callback) {
        ArrayList<Event> events = new ArrayList<>();

        db.collection("events")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Event event = document.toObject(Event.class);
                            if(event.getUserId().equals(userId)) {
                                events.add(event);
                            }
                        }
                        callback.onEventFetch(events);
                    } else {
                        Log.w("REZ_DB", "Error getting documents.", task.getException());
                        callback.onEventFetch(null);
                    }
                });
    }

    public void getById(EventRepository.EventByIdFetchCallback callback, String id) {
        db.collection("events").document(id)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Event event = document.toObject(Event.class);
                            callback.onEventByIdFetch(event);
                        } else {
                            Log.d("REZ_DB", "No such document");
                            callback.onEventByIdFetch(null);
                        }
                    } else {
                        Log.w("REZ_DB", "Error getting document.", task.getException());
                        callback.onEventByIdFetch(null);
                    }
                });
    }

    public interface EventFetchCallback {
        default void onEventFetch(ArrayList<Event> events) {
        }
        default void onResult(boolean contains) {
        }
    }

    public interface EventByIdFetchCallback {
        default void onEventByIdFetch(Event event) {
        }
        default void onResult(boolean contains) {
        }
    }

    public static String generateId() {
        return "event_" + System.currentTimeMillis();
    }
}
