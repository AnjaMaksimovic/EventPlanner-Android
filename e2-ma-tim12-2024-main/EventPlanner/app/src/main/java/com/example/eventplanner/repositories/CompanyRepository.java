package com.example.eventplanner.repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.eventplanner.model.Company;
import com.example.eventplanner.repositories.interfaces.ICompanyRepository;
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

public class CompanyRepository implements ICompanyRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void Create(Company company) {
        db.collection("companies")
                .add(company)
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
    public void Delete(String companyId) {
        db.collection("companies")
                .whereEqualTo("id", companyId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        db.collection("companies").document(document.getId())
                                .delete()
                                .addOnSuccessListener(aVoid -> Log.d("REZ_DB", "DocumentSnapshot successfully deleted!"))
                                .addOnFailureListener(e -> Log.w("REZ_DB", "Error deleting document", e));
                    }
                })
                .addOnFailureListener(e -> Log.w("REZ_DB", "Error getting documents", e));
    }

    @Override
    public void Update(Company company) {
        db.collection("companies")
                .document(company.getId())
                .set(company)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("REZ_DB", "DocumentSnapshot updated with ID: " + company.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("REZ_DB", "Error updating document", e);
                    }
                });
    }

    @Override
    public void GetByOwnerId(String ownerId, ICompanyRepository.GetCompaniesCallback callback) {
        ArrayList<Company> companies = new ArrayList<>();
        db.collection("companies")
                .whereEqualTo("ownerId", ownerId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("REZ_DB", document.getId() + " => " + document.getData());
                                Company company = document.toObject(Company.class);
                                companies.add(company);
                                callback.OnGetCompanies(companies);
                                return;
                            }
                        } else {
                            Log.w("REZ_DB", "Error getting documents.", task.getException());
                            callback.OnGetCompanies(null);
                        }
                    }
                });
    }

    public void getAllCompanies(ICompanyRepository.GetCompaniesCallback callback) {
        ArrayList<Company> companies = new ArrayList<>();

        db.collection("companies")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Company company = document.toObject(Company.class);
                            companies.add((company));
                            Log.d("Uzeo sam kompanije", companies.toString());
                        }

                        callback.OnGetCompanies(companies);
                    } else {
                        Log.w("REZ_DB", "Error getting documents.", task.getException());
                        // Invoke the callback with null if an error occurs
                        callback.OnGetCompanies(null);
                    }
                });
    }

    public void getAllUnhandledCompanies(ICompanyRepository.GetCompaniesCallback callback) {
        ArrayList<Company> companies = new ArrayList<>();

        db.collection("companies")
                .whereEqualTo("requestHandled", false)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Company company = document.toObject(Company.class);
                            companies.add(company);
                            Log.d("Uzeo sam kompanije", companies.toString());
                        }

                        callback.OnGetCompanies(companies);
                    } else {
                        Log.w("REZ_DB", "Error getting documents.", task.getException());
                        // Invoke the callback with null if an error occurs
                        callback.OnGetCompanies(null);
                    }
                });
    }

    public void getAllCompaniesBySearch(String query, GetCompaniesCallback callback) {
        Log.d("REZ_DB", "Query: " + query); // Log the query
        ArrayList<Company> companies = new ArrayList<>();

        // Split the query into individual words
        String[] queryWords = query.toLowerCase().split("\\s+");

        db.collection("companies")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Company company = document.toObject(Company.class);
                            if (!company.getRequestHandled()) {
                                // Check if all query words are contained in any of the fields
                                boolean allWordsMatch = true;
                                for (String word : queryWords) {
                                    if (!(company.getOwnerName().toLowerCase().contains(word) ||
                                            company.getOwnerLastname().toLowerCase().contains(word) ||
                                            company.getOwnerEmail().toLowerCase().contains(word) ||
                                            company.getName().toLowerCase().contains(word) ||
                                            company.getEmail().toLowerCase().contains(word))) {
                                        allWordsMatch = false;
                                        break;
                                    }
                                }

                                if (allWordsMatch) {
                                    companies.add(company);
                                }
                            }
                        }
                        // Invoke the callback with the filtered companies list
                        callback.OnGetCompanies(companies);
                    } else {
                        Log.w("REZ_DB", "Error getting documents.", task.getException());
                        // Invoke the callback with null if an error occurs
                        callback.OnGetCompanies(null);
                    }
                });
    }

}