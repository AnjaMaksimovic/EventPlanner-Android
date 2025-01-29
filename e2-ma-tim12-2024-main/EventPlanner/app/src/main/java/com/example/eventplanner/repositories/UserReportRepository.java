package com.example.eventplanner.repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.eventplanner.model.UserReport;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class UserReportRepository {
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void createUserReport(UserReport newReport){
        newReport.setId(generateId());
        db.collection("user_reports")
                .document(newReport.getId())
                .set(newReport)

                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("REZ_DB", "DocumentSnapshot added with ID: " + newReport.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("REZ_DB", "Error adding document", e);
                    }
                });
    }

    public void updateUserReport(UserReport report) {
        db.collection("user_reports")
                .document(report.getId())
                .set(report)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("REZ_DB", "DocumentSnapshot updated with ID: " + report.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("REZ_DB", "Error updating document", e);
                    }
                });
    }

    public void getUserReports(UserReportRepository.UserReportFetchCallback callback) {
        ArrayList<UserReport> reports = new ArrayList<>();

        db.collection("user_reports")
                .whereEqualTo("status", "reported")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            UserReport report = document.toObject(UserReport.class);
                            reports.add(report);
                        }
                        // Invoke the callback with the products list
                        callback.onUserReportFetch(reports);
                    } else {
                        Log.w("REZ_DB", "Error getting documents.", task.getException());
                        // Invoke the callback with null if an error occurs
                        callback.onUserReportFetch(null);
                    }
                });
    }

    public interface UserReportFetchCallback {
        default void onUserReportFetch(ArrayList<UserReport> reports) {
        }
        default void onResult(boolean contains) {
        }
    }

    public static String generateId() {
        return "user_report_" + System.currentTimeMillis();
    }
}
