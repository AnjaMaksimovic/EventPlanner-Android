package com.example.eventplanner.repositories;
import android.util.Log;
import androidx.annotation.NonNull;
import com.example.eventplanner.model.EventActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class EventActivityRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void Create(EventActivity eventActivity){
        eventActivity.setId(generateId());
        db.collection("eventActivities")
                .document(eventActivity.getId())
                .set(eventActivity)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("REZ_DB", "DocumentSnapshot added with ID: " + eventActivity.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("REZ_DB", "Error adding document", e);
                    }
                });
    }

    public void Delete(String id){
        DocumentReference docRef = db.collection("eventActivities").document(id);
        docRef.update("deleted", true)
                .addOnSuccessListener(aVoid -> Log.d("REZ_DB", "Service successfully changed"))
                .addOnFailureListener(e -> Log.w("REZ_DB", "Error getting documents.", e));
    }

    public void GetAllEventActivities(EventActivityRepository.EventActivityFetchCallback callback) {
        ArrayList<EventActivity> eventActivities = new ArrayList<>();

        db.collection("eventActivities")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            EventActivity eventActivity = document.toObject(EventActivity.class);
                            eventActivities.add(eventActivity);
                        }
                        callback.onEventActivityFetch(eventActivities);
                    } else {
                        Log.w("REZ_DB", "Error getting documents.", task.getException());
                        callback.onEventActivityFetch(null);
                    }
                });
    }

    public interface EventActivityFetchCallback {
        default void onEventActivityFetch(ArrayList<EventActivity> eventActivities) {
        }
        default void onResult(boolean contains) {
        }
    }

    public static String generateId() {
        return "event_activity_" + System.currentTimeMillis();
    }
}
