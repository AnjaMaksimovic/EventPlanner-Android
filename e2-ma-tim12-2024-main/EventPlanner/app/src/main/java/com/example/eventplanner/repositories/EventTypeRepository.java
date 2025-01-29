package com.example.eventplanner.repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.eventplanner.model.Event;
import com.example.eventplanner.model.EventType;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Random;

public class EventTypeRepository {
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static void createEventType(EventType eventType){
        eventType.setId(generateId());
        db.collection("eventTypes")
                .document(eventType.getId())
                .set(eventType)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("REZ_DB", "DocumentSnapshot added with ID: " + eventType.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("REZ_DB", "Error adding document", e);
                    }
                });
    }

    public static void delete(String id){
        DocumentReference docRef = db.collection("eventTypes").document(id);
        docRef.update("deleted", true)
                .addOnSuccessListener(aVoid -> Log.d("REZ_DB", "Service successfully changed"))
                .addOnFailureListener(e -> Log.w("REZ_DB", "Error getting documents.", e));
    }

    public void getAllEventTypes(EventTypeFetchCallback callback) {
        ArrayList<EventType> eventTypes = new ArrayList<>();

        db.collection("eventTypes")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            EventType eventType = document.toObject(EventType.class);
                            eventTypes.add(eventType);
                        }
                        callback.onEventTypeFetch(eventTypes);
                    } else {
                        Log.w("REZ_DB", "Error getting documents.", task.getException());
                        callback.onEventTypeFetch(null);
                    }
                });
    }

    public interface EventTypeFetchCallback {
        default void onEventTypeFetch(ArrayList<EventType> eventTypes) {
        }
        default void onResult(boolean contains) {
        }
    }

    public static String generateId() {
        return "eventType_" + System.currentTimeMillis();
    }
}
