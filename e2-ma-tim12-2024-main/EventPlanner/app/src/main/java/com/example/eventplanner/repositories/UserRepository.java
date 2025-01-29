package com.example.eventplanner.repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.eventplanner.model.User;
import com.example.eventplanner.repositories.interfaces.IUserRepository;
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

public class UserRepository implements IUserRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void Create(User user){
        db.collection("users")
                .add(user)
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
    public void GetByEmail(String email, GetUsersCallback callback) {
        ArrayList<User> users = new ArrayList<>();
        db.collection("users")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("REZ_DB", document.getId() + " => " + document.getData());
                                User user = document.toObject(User.class);
                                users.add(user);
                                callback.OnGetUsers(users);
                                return;
                            }
                        } else {
                            Log.w("REZ_DB", "Error getting documents.", task.getException());
                            callback.OnGetUsers(null);
                        }
                    }
                });
    }

    @Override
    public void Delete(String userId) {
        db.collection("users")
                .whereEqualTo("id", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        db.collection("users").document(document.getId())
                                .delete()
                                .addOnSuccessListener(aVoid -> Log.d("REZ_DB", "DocumentSnapshot successfully deleted!"))
                                .addOnFailureListener(e -> Log.w("REZ_DB", "Error deleting document", e));
                    }
                })
                .addOnFailureListener(e -> Log.w("REZ_DB", "Error getting documents", e));
    }

    //TODO: Change user.setActive call
    @Override
    public void Deactivate(String userId, GetUsersCallback callback) {
        ArrayList<User> users = new ArrayList<>();
        db.collection("users")
                .whereEqualTo("id", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        db.collection("users").document(document.getId())
                                .update("active", false)
                                .addOnSuccessListener(aVoid -> {
                                    User user = document.toObject(User.class);
                                    user.setActive(false);
                                    users.add(user);
                                    callback.OnGetUsers(users);
                                    Log.d("REZ_DB", "DocumentSnapshot successfully updated!");
                                })
                                .addOnFailureListener(e -> Log.w("REZ_DB", "Error updating document", e));
                    }
                })
                .addOnFailureListener(e -> Log.w("REZ_DB", "Error getting documents", e));
    }

    @Override
    public void Activate(String userId, GetUsersCallback callback) {
        ArrayList<User> users = new ArrayList<>();
        db.collection("users")
                .whereEqualTo("id", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        db.collection("users").document(document.getId())
                                .update("active", true)
                                .addOnSuccessListener(aVoid -> {
                                    User user = document.toObject(User.class);
                                    user.setActive(true);
                                    users.add(user);
                                    callback.OnGetUsers(users);
                                    Log.d("REZ_DB", "DocumentSnapshot successfully updated!");
                                })
                                .addOnFailureListener(e -> Log.w("REZ_DB", "Error updating document", e));
                    }
                })
                .addOnFailureListener(e -> Log.w("REZ_DB", "Error getting documents", e));
    }

    @Override
    public void BlockUser(String userId, BlockUserCallback callback) {
        db.collection("users")
                .whereEqualTo("id", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        callback.onBlockFailure(new Exception("User not found"));
                        return;
                    }

                    DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                    db.collection("users").document(document.getId())
                            .update("blocked", true)
                            .addOnSuccessListener(aVoid -> {
                                Log.d("REZ_DB", "DocumentSnapshot successfully updated!");
                                callback.onBlockSuccess();
                            })
                            .addOnFailureListener(e -> {
                                Log.w("REZ_DB", "Error updating document", e);
                                callback.onBlockFailure(e);
                            });
                })
                .addOnFailureListener(e -> {
                    Log.w("REZ_DB", "Error getting documents", e);
                    callback.onBlockFailure(e);
                });
    }

    @Override
    public void getAllAdmins(GetUsersCallback callback) {
        ArrayList<User> users = new ArrayList<>();
        db.collection("users")
                .whereEqualTo("role", "admin")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("REZ_DB", document.getId() + " => " + document.getData());
                                User user = document.toObject(User.class);
                                users.add(user);
                            }
                            callback.OnGetUsers(users);
                        } else {
                            Log.w("REZ_DB", "Error getting documents.", task.getException());
                            callback.OnGetUsers(null);
                        }
                    }
                });
    }
}