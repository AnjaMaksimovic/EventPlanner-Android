package com.example.eventplanner.repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.eventplanner.model.Category;
import com.example.eventplanner.model.Product;
import com.example.eventplanner.model.Subcategory;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class CategoryRepository {
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void createCategory(Category newCategory) {
        newCategory.setId(generateId());
        db.collection("categories")
                .document(newCategory.getId())
                .set(newCategory)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("REZ_DB", "DocumentSnapshot added with ID: " + newCategory.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("REZ_DB", "Error adding document", e);
                    }
                });
    }

    public void getCategoryById(String categoryId, CategoryRepository.CategoryFetchCallback callback) {
        db.collection("categories")
                .document(categoryId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Category category = documentSnapshot.toObject(Category.class);
                        callback.onCategoryFetched(category, null);
                    } else {
                        callback.onCategoryFetched(null, "Category with ID " + categoryId + " does not exist.");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.w("REZ_DB", "Error getting document.", e);
                    callback.onCategoryFetched(null, "Error getting category with ID " + categoryId);
                });
    }

    public static void updateCategory(Category c) {
        db.collection("categories")
                .document(c.getId())
                .set(c)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("REZ_DB", "DocumentSnapshot updated with ID: " + c.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("REZ_DB", "Error updating document", e);
                    }
                });
    }


    public void getAllCategories(CategoryRepository.CategoryFetchCallback callback) {
        ArrayList<Category> categories = new ArrayList<>();

        db.collection("categories")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Category category = document.toObject(Category.class);
                            categories.add((category));
                            Log.d("Uzeo sam kategorije", categories.toString());
                        }

                        callback.onCategoryFetch(categories);
                    } else {
                        Log.w("REZ_DB", "Error getting documents.", task.getException());
                        // Invoke the callback with null if an error occurs
                        callback.onCategoryFetch(null);
                    }
                });
    }

    public interface CategoryFetchCallback {
        default void onCategoryFetch(ArrayList<Category> categories) {

        }
        default void onCategoryFetched(Category category, String errorMessage) {

        }
        default void onUpdateSuccess() {}
        default void onUpdateFailure(String errorMessage) {}
    }

    public static String generateId() {
        return "category_" + System.currentTimeMillis();
    }
}
