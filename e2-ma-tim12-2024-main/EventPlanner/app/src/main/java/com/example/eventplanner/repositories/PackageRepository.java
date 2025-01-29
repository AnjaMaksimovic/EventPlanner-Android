package com.example.eventplanner.repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.eventplanner.model.Company;
import com.example.eventplanner.model.Package;
import com.example.eventplanner.model.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.bouncycastle.util.Pack;

import java.util.ArrayList;

public class PackageRepository {
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void createPackage(Package newPackage){
        newPackage.setId(generateId());
        db.collection("packages")
                .document(newPackage.getId())
                .set(newPackage)

                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("REZ_DB", "DocumentSnapshot added with ID: " + newPackage.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("REZ_DB", "Error adding document", e);
                    }
                });
    }

    public void updatePackage(Package s) {
        db.collection("packages")
                .document(s.getId())
                .set(s)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("REZ_DB", "DocumentSnapshot updated with ID: " + s.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("REZ_DB", "Error updating document", e);
                    }
                });
    }

    public void getAllPackages(PackageRepository.PackageFetchCallback callback) {
        ArrayList<Package> packages = new ArrayList<>();

        db.collection("packages")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Package package1 = document.toObject(Package.class);
                            if (!package1.isDeleted()) {
                                packages.add(package1);
                            }
                        }
                        // Invoke the callback with the products list
                        callback.onPackageFetch(packages);
                    } else {
                        Log.w("REZ_DB", "Error getting documents.", task.getException());
                        // Invoke the callback with null if an error occurs
                        callback.onPackageFetch(null);
                    }
                });
    }

    public void getPackageById(String packageId, PackageFetchCallback callback) {
        ArrayList<Package> packages = new ArrayList<>();
        db.collection("packages")
                .whereEqualTo("id", packageId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("REZ_DB", document.getId() + " => " + document.getData());
                                Package aPackage = document.toObject(Package.class);
                                packages.add(aPackage);
                                callback.onPackageFetch(packages);
                                return;
                            }
                        } else {
                            Log.w("REZ_DB", "Error getting documents.", task.getException());
                            callback.onPackageFetch(null);
                        }
                    }
                });
    }

    public interface PackageFetchCallback {
        default void onPackageFetch(ArrayList<Package> packages) {
        }
        default void onResult(boolean contains) {
        }
    }

    public static String generateId() {
        return "package_" + System.currentTimeMillis();
    }
}
