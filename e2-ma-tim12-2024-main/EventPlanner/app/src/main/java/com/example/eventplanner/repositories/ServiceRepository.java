package com.example.eventplanner.repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.eventplanner.model.Service;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ServiceRepository {
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void createService(Service newService){
        newService.setId(generateId());
        db.collection("services")
                .document(newService.getId())
                .set(newService)

                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("REZ_DB", "DocumentSnapshot added with ID: " + newService.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("REZ_DB", "Error adding document", e);
                    }
                });
    }

    public static void updateService(Service s) {
        db.collection("services")
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

    public void getAllServices(ServiceFetchCallback callback) {
        ArrayList<Service> services = new ArrayList<>();

        db.collection("services")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Service service = document.toObject(Service.class);
                            if (!service.isDeleted()) {
                                services.add(service);
                            }
                        }
                        // Invoke the callback with the products list
                        callback.onServiceFetch(services);
                    } else {
                        Log.w("REZ_DB", "Error getting documents.", task.getException());
                        // Invoke the callback with null if an error occurs
                        callback.onServiceFetch(null);
                    }
                });
    }

    public void getAllVisibleServices(ServiceFetchCallback callback) {
        ArrayList<Service> services = new ArrayList<>();

        db.collection("services")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Service service = document.toObject(Service.class);
                            if (!service.isDeleted() && service.isVisibility()) {
                                services.add(service);
                            }
                        }
                        // Invoke the callback with the products list
                        callback.onServiceFetch(services);
                    } else {
                        Log.w("REZ_DB", "Error getting documents.", task.getException());
                        // Invoke the callback with null if an error occurs
                        callback.onServiceFetch(null);
                    }
                });
    }

    public void getAllServicesBySearchName(String query, ServiceFetchCallback callback) {
        Log.d("REZ_DB", "Query: " + query); // Log the query
        ArrayList<Service> services = new ArrayList<>();

        db.collection("services")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Service service = document.toObject(Service.class);
                            if (!service.isDeleted() && service.getName().toLowerCase().contains(query.toLowerCase())) {
                                services.add(service);
                            }
                        }
                        // Invoke the callback with the filtered products list
                        callback.onServiceFetch(services);
                    } else {
                        Log.w("REZ_DB", "Error getting documents.", task.getException());
                        // Invoke the callback with null if an error occurs
                        callback.onServiceFetch(null);
                    }
                });
    }

    public void getById(ServiceRepository.ServiceByIdFetchCallback callback, String id) {
        db.collection("services").document(id)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Service service = document.toObject(Service.class);
                            callback.onServiceByIdFetch(service);
                        } else {
                            Log.d("REZ_DB", "No such document");
                            callback.onServiceByIdFetch(null);
                        }
                    } else {
                        Log.w("REZ_DB", "Error getting document.", task.getException());
                        callback.onServiceByIdFetch(null);
                    }
                });
    }

    public void SetUnavailability(String serviceId, ServiceRepository.ServiceFetchCallback callback) {
        ArrayList<Service> services = new ArrayList<>();
        db.collection("services")
                .whereEqualTo("id", serviceId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        db.collection("services").document(document.getId())
                                .update("availiability", false)
                                .addOnSuccessListener(aVoid -> {
                                    Service service = document.toObject(Service.class);
                                    service.setAvailability(false);
                                    services.add(service);
                                    callback.onServiceFetch(services);
                                    Log.d("REZ_DB", "DocumentSnapshot successfully updated!");
                                })
                                .addOnFailureListener(e -> Log.w("REZ_DB", "Error updating document", e));
                    }
                })
                .addOnFailureListener(e -> Log.w("REZ_DB", "Error getting documents", e));
    }

    public interface ServiceFetchCallback {
        default void onServiceFetch(ArrayList<Service> services) {
        }
        default void onResult(boolean contains) {
        }
    }

    public interface ServiceByIdFetchCallback {
        default void onServiceByIdFetch(Service service) {
        }
        default void onResult(boolean contains) {
        }
    }

    public static String generateId() {
        return "service_" + System.currentTimeMillis();
    }

}
