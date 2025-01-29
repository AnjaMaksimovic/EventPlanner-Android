package com.example.eventplanner.repositories.system;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.eventplanner.model.enums.NotificationStatus;
import com.example.eventplanner.model.reviews.Review;
import com.example.eventplanner.model.reviews.ReviewReport;
import com.example.eventplanner.model.system.Notification;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class NotificationRepository implements INotificationRepository{
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    public void Create(Notification notification) {
        notification.setId();
        db.collection("notifications")
                .add(notification)
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

    @Override
    public void GetAllByReceiver(String receiverId, GetNotificationsCallback callback) {
        ArrayList<Notification> notifications = new ArrayList<>();
        db.collection("notifications")
                .whereEqualTo("receiverId", receiverId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("REZ_DB", document.getId() + " => " + document.getData());
                                Notification notification = document.toObject(Notification.class);
                                notifications.add(notification);
                            }
                            callback.OnGetNotifications(notifications);
                        } else {
                            Log.w("REZ_DB", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    @Override
    public void Update(String id, NotificationStatus newStatus, GetNotificationsCallback callback) {
        ArrayList<Notification> notifications = new ArrayList<>();
        db.collection("notifications")
                .whereEqualTo("id", id)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        db.collection("notifications").document(document.getId())
                                .update("status", newStatus)
                                .addOnSuccessListener(aVoid -> {
                                    Notification notification = document.toObject(Notification.class);
                                    notification.setStatus(newStatus);
                                    notifications.add(notification);
                                    callback.OnGetNotifications(notifications);
                                    Log.d("REZ_DB", "DocumentSnapshot successfully updated!");
                                })
                                .addOnFailureListener(e -> Log.w("REZ_DB", "Error updating document", e));
                    }
                })
                .addOnFailureListener(e -> Log.w("REZ_DB", "Error getting documents", e));
    }
}