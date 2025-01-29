package com.example.eventplanner.repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.eventplanner.model.CompanyReport;
import com.example.eventplanner.model.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class CompanyReportRepository {
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void createCompanyReport(CompanyReport newReport){
        newReport.setId(generateId());
        db.collection("company_reports")
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

    public void getAllCompanyReports(CompanyReportRepository.CompanyReportFetchCallback callback) {
        ArrayList<CompanyReport> reports = new ArrayList<>();

        db.collection("company_reports")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            CompanyReport report = document.toObject(CompanyReport.class);
                                reports.add(report);
                        }
                        // Invoke the callback with the products list
                        callback.onCompanyReportFetch(reports);
                    } else {
                        Log.w("REZ_DB", "Error getting documents.", task.getException());
                        // Invoke the callback with null if an error occurs
                        callback.onCompanyReportFetch(null);
                    }
                });
    }

    public interface CompanyReportFetchCallback {
        default void onCompanyReportFetch(ArrayList<CompanyReport> reports) {
        }
        default void onResult(boolean contains) {
        }
    }

    public static String generateId() {
        return "company_report_" + System.currentTimeMillis();
    }
}
