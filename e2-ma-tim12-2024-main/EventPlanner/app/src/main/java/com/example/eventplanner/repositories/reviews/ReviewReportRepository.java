package com.example.eventplanner.repositories.reviews;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.eventplanner.model.enums.ReportStatus;
import com.example.eventplanner.model.reviews.ReviewReport;
import com.example.eventplanner.repositories.reviews.interfaces.IReviewReportRepository;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ReviewReportRepository implements IReviewReportRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    public void Create(ReviewReport report) {
        db.collection("reviewReports")
                .add(report)
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
    public void Update(String reportId, ReportStatus newStatus, GetReportsCallback callback) {
        ArrayList<ReviewReport> reports = new ArrayList<>();
        db.collection("reviewReports")
                .whereEqualTo("id", reportId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        db.collection("reviewReports").document(document.getId())
                                .update("status", newStatus)
                                .addOnSuccessListener(aVoid -> {
                                    ReviewReport report = document.toObject(ReviewReport.class);
                                    report.setStatus(newStatus);
                                    reports.add(report);
                                    callback.OnGetReports(reports);
                                    Log.d("REZ_DB", "DocumentSnapshot successfully updated!");
                                })
                                .addOnFailureListener(e -> Log.w("REZ_DB", "Error updating document", e));
                    }
                })
                .addOnFailureListener(e -> Log.w("REZ_DB", "Error getting documents", e));
    }

    @Override
    public void GetAll(GetReportsCallback callback) {
        ArrayList<ReviewReport> reports = new ArrayList<>();
        db.collection("reviewReports")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            ReviewReport report = document.toObject(ReviewReport.class);
                            reports.add(report);
                        }
                        callback.OnGetReports(reports);
                    } else {
                        Log.w("REZ_DB", "Error getting documents.", task.getException());
                        // Invoke the callback with null if an error occurs
                    }
                });
    }
}