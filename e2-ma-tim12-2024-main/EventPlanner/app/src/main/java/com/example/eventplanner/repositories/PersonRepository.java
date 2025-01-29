package com.example.eventplanner.repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.eventplanner.model.Event;
import com.example.eventplanner.model.Person;
import com.example.eventplanner.repositories.interfaces.IPersonRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PersonRepository implements IPersonRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void Create(Person person) {
        db.collection("people")
                .add(person)
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
    public void Delete(String personId) {
        db.collection("people")
                .whereEqualTo("id", personId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        db.collection("people").document(document.getId())
                                .delete()
                                .addOnSuccessListener(aVoid -> Log.d("REZ_DB", "DocumentSnapshot successfully deleted!"))
                                .addOnFailureListener(e -> Log.w("REZ_DB", "Error deleting document", e));
                    }
                })
                .addOnFailureListener(e -> Log.w("REZ_DB", "Error getting documents", e));
    }



    @Override
    public void Update(String personId, Person person) {
        Map<String, Object> personData = new HashMap<>();
        personData.put("name", person.getName());
        personData.put("lastname", person.getLastname());
        personData.put("address", person.getAddress());
        personData.put("photoPath", person.getPhotoPath());
        personData.put("phoneNumber", person.getPhoneNumber());

        db.collection("people")
                .whereEqualTo("id", personId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        db.collection("people").document(document.getId())
                                .set(personData, SetOptions.merge())
                                .addOnSuccessListener(aVoid -> Log.d("REZ_DB", "DocumentSnapshot successfully updated!"))
                                .addOnFailureListener(e -> Log.w("REZ_DB", "Error updating document", e));
                    }
                })
                .addOnFailureListener(e -> Log.w("REZ_DB", "Error getting documents", e));
    }


    public void updateForFavouriteList(String personId, Person person) {
        Map<String, Object> personData = new HashMap<>();
        personData.put("favouriteProducts", person.getFavouriteProducts());
        personData.put("favouriteServices", person.getFavouriteServices());

        db.collection("people")
                .whereEqualTo("id", personId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        db.collection("people").document(document.getId())
                                .set(personData, SetOptions.merge())
                                .addOnSuccessListener(aVoid -> Log.d("REZ_DB", "DocumentSnapshot successfully updated!"))
                                .addOnFailureListener(e -> Log.w("REZ_DB", "Error updating document", e));
                    }
                })
                .addOnFailureListener(e -> Log.w("REZ_DB", "Error getting documents", e));
    }

//    public void updateForFavouriteList(Person person) {
//        db.collection("people")
//                .document(person.getId())
//                .set(person)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Log.d("REZ_DB", "DocumentSnapshot updated with ID: " + person.getId());
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w("REZ_DB", "Error updating document", e);
//                    }
//                });
//    }

    @Override
    public void Get(String personId, GetPersonsCallback callback) {
        ArrayList<Person> people = new ArrayList<>();
        db.collection("people")
                .whereEqualTo("id", personId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("REZ_DB", document.getId() + " => " + document.getData());
                                people.add(document.toObject(Person.class));
                                break;
                            }
                            callback.OnGetPeople(people);
                        } else {
                            Log.w("REZ_DB", "Error getting documents.", task.getException());
                            callback.OnGetPeople(null);
                        }
                    }
                });

    }

    @Override
    public void getPersonByUserId(String userId, GetPersonsCallback callback) {
        ArrayList<Person> people = new ArrayList<>();
        db.collection("people")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("REZ_DB", document.getId() + " => " + document.getData());
                                people.add(document.toObject(Person.class));
                                break;
                            }
                            callback.OnGetPeople(people);
                        } else {
                            Log.w("REZ_DB", "Error getting documents.", task.getException());
                            callback.OnGetPeople(null);
                        }
                    }
                });

    }

    @Override
    public void GetAllByCompany(String companyId, GetPersonsCallback callback) {
        ArrayList<Person> people = new ArrayList<>();
        db.collection("people")
                .whereEqualTo("companyId", companyId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("REZ_DB", document.getId() + " => " + document.getData());
                                people.add(document.toObject(Person.class));
                            }
                            callback.OnGetPeople(people);
                        } else {
                            Log.w("REZ_DB", "Error getting documents.", task.getException());
                            callback.OnGetPeople(null);
                        }
                    }
                });
    }

    @Override
    public void SearchPeopleByCompany(String companyId, String name, String lastname, String email, GetPersonsCallback callback) {
        ArrayList<Person> people = new ArrayList<>();
        db.collection("people")
                .whereEqualTo("companyId", companyId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Person person = document.toObject(Person.class);
                                people.add(person);
                            }
                            if(name != null && !name.isEmpty()){
                                people.removeIf(person -> !person.getName().toLowerCase().startsWith(name.toLowerCase()));
                            }
                            if(lastname != null && !lastname.isEmpty()){
                                people.removeIf(person -> !person.getLastname().toLowerCase().startsWith(lastname.toLowerCase()));
                            }
                            if(email != null && !email.isEmpty()){
                                people.removeIf(person -> !person.getEmail().toLowerCase().startsWith(email.toLowerCase()));
                            }
                            callback.OnGetPeople(people);
                        } else {
                            Log.w("REZ_DB", "Error getting documents.", task.getException());
                            callback.OnGetPeople(null);
                        }
                    }
                });
    }

    public void getAllPupv(PersonRepository.GetPersonsCallback callback) {
        ArrayList<Person> people = new ArrayList<>();

        db.collection("people")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Person person = document.toObject(Person.class);
                            if (person.getWorkSchedule() == null && person.getCompanyId() != null) {
                                people.add(person);
                            }
                        }
                        // Invoke the callback with the products list
                        callback.OnGetPeople(people);
                    } else {
                        Log.w("REZ_DB", "Error getting documents.", task.getException());
                        // Invoke the callback with null if an error occurs
                        callback.OnGetPeople(null);
                    }
                });
    }


}