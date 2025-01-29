package com.example.eventplanner.repositories.reviews;

import android.util.Log;

import androidx.annotation.NonNull;
import com.example.eventplanner.model.reviews.Review;
import com.example.eventplanner.repositories.reviews.interfaces.GetReviewsCallback;
import com.example.eventplanner.repositories.reviews.interfaces.IReviewRepository;
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

public class ReviewRepository implements IReviewRepository {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    public void Create(Review review) {
        db.collection("reviews")
                .add(review)
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
    public void Delete(String reviewId) {
        db.collection("reviews")
                .whereEqualTo("id", reviewId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        db.collection("reviews").document(document.getId())
                                .delete()
                                .addOnSuccessListener(aVoid -> Log.d("REZ_DB", "DocumentSnapshot successfully deleted!"))
                                .addOnFailureListener(e -> Log.w("REZ_DB", "Error deleting document", e));
                    }
                })
                .addOnFailureListener(e -> Log.w("REZ_DB", "Error getting documents", e));
    }

    @Override
    public void GetAllByCompany(String companyId, GetReviewsCallback callback) {
        ArrayList<Review> reviews = new ArrayList<>();
        db.collection("reviews")
                .whereEqualTo("companyId", companyId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("REZ_DB", document.getId() + " => " + document.getData());
                                Review review = document.toObject(Review.class);
                                reviews.add(review);
                            }
                            callback.OnReviewsGet(reviews);
                        } else {
                            Log.w("REZ_DB", "Error getting documents.", task.getException());
                            callback.OnReviewsGet(null);
                        }
                    }
                });
    }
}