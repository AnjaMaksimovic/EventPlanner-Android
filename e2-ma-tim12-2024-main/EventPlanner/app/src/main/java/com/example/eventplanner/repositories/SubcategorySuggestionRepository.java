package com.example.eventplanner.repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.eventplanner.model.SubcategorySuggestion;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class SubcategorySuggestionRepository {

    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void createSuggestion(SubcategorySuggestion subcategorySuggestion) {
        subcategorySuggestion.setId(generateId());
        db.collection("subcategory_suggestions")
                .document(subcategorySuggestion.getId())
                .set(subcategorySuggestion)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("REZ_DB", "DocumentSnapshot added with ID: " + subcategorySuggestion.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("REZ_DB", "Error adding document", e);
                    }
                });

    }

    public static String generateId() {
        return "subcategory_suggestion_" + System.currentTimeMillis();
    }
}
