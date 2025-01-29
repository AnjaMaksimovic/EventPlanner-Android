package com.example.eventplanner.repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.eventplanner.model.Category;
import com.example.eventplanner.model.Subcategory;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class SubcategoryRepository {

    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void createSubcategory(Subcategory newSubcategory) {
        newSubcategory.setId(generateId());
        db.collection("subcategories")
                .document(newSubcategory.getId())
                .set(newSubcategory)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("REZ_DB", "DocumentSnapshot added with ID: " + newSubcategory.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("REZ_DB", "Error adding document", e);
                    }
                });
    }

    public void getAllSubcategories(SubcategoryRepository.SubcategoryFetchCallback callback) {
        ArrayList<Subcategory> subcategories = new ArrayList<>();

        db.collection("subcategories")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Subcategory subcategory = document.toObject(Subcategory.class);
                            subcategories.add((subcategory));
                        }

                        callback.onSubcategoryFetch(subcategories);
                    } else {
                        Log.w("REZ_DB", "Error getting documents.", task.getException());
                        // Invoke the callback with null if an error occurs
                        callback.onSubcategoryFetch(null);
                    }
                });
    }

    public void getSubcategoriesByCategoryId(String categoryId, SubcategoryRepository.SubcategoryFetchCallback callback) {
        ArrayList<Subcategory> subcategories = new ArrayList<>();

        db.collection("subcategories")
                .whereEqualTo("categoryId", categoryId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Subcategory subcategory = document.toObject(Subcategory.class);
                            subcategories.add(subcategory);
                        }

                        callback.onSubcategoryFetch(subcategories);
                    } else {
                        Log.w("REZ_DB", "Error getting documents.", task.getException());
                        // Invoke the callback with null if an error occurs
                        callback.onSubcategoryFetch(null);
                    }
                });
    }


    public static void updateSubcategory(Subcategory s) {
        String subcategoryId = s.getId();
        if (subcategoryId != null) {
            db.collection("subcategories")
                    .document(subcategoryId)
                    .set(s)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("REZ_DB", "DocumentSnapshot updated with ID: " + subcategoryId);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("REZ_DB", "Error updating document", e);
                        }
                    });
        } else {
            Log.e("REZ_DB", "Subcategory ID is null");
        }
    }


    public interface SubcategoryFetchCallback {
        default void onSubcategoryFetch(ArrayList<Subcategory> subcategories) {

        }
        default void onSubcategoryFetched(Subcategory subcategory, String errorMessage) {

        }
        default void onUpdateSuccess() {}
        default void onUpdateFailure(String errorMessage) {}
    }

    public static String generateId() {
        return "subcategory_" + System.currentTimeMillis();
    }

}
