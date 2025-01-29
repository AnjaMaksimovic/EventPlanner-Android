package com.example.eventplanner.repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.eventplanner.model.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ProductRepository {
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void createProduct(Product newProduct){
        newProduct.setId(generateId());
        db.collection("products")
                .document(newProduct.getId())
                .set(newProduct)

                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("REZ_DB", "DocumentSnapshot added with ID: " + newProduct.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("REZ_DB", "Error adding document", e);
                    }
                });
    }

    public static void updateProduct(Product p) {
        db.collection("products")
                .document(p.getId())
                .set(p)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("REZ_DB", "DocumentSnapshot updated with ID: " + p.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("REZ_DB", "Error updating document", e);
                    }
                });
    }

    public void getAllProducts(ProductFetchCallback callback) {
        ArrayList<Product> products = new ArrayList<>();

        db.collection("products")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Product product = document.toObject(Product.class);
                            if (!product.isDeleted()) {
                                products.add(product);
                            }
                        }
                        // Invoke the callback with the products list
                        callback.onProductFetch(products);
                    } else {
                        Log.w("REZ_DB", "Error getting documents.", task.getException());
                        // Invoke the callback with null if an error occurs
                        callback.onProductFetch(null);
                    }
                });
    }

    public void getAllVisibleProducts(ProductFetchCallback callback) {
        ArrayList<Product> products = new ArrayList<>();

        db.collection("products")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Product product = document.toObject(Product.class);
                            if (!product.isDeleted() && product.isVisibility()) {
                                products.add(product);
                            }
                        }
                        // Invoke the callback with the products list
                        callback.onProductFetch(products);
                    } else {
                        Log.w("REZ_DB", "Error getting documents.", task.getException());
                        // Invoke the callback with null if an error occurs
                        callback.onProductFetch(null);
                    }
                });
    }

    public void getAllProductsBySearchName(String query, ProductFetchCallback callback) {
        Log.d("REZ_DB", "Query: " + query); // Log the query
        ArrayList<Product> products = new ArrayList<>();

        db.collection("products")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Product product = document.toObject(Product.class);
                            if (!product.isDeleted() && product.getName().toLowerCase().contains(query.toLowerCase())) {
                                products.add(product);
                            }
                        }
                        // Invoke the callback with the filtered products list
                        callback.onProductFetch(products);
                    } else {
                        Log.w("REZ_DB", "Error getting documents.", task.getException());
                        // Invoke the callback with null if an error occurs
                        callback.onProductFetch(null);
                    }
                });
    }

    public void getAllVisibleProductsBySearchName(String query, ProductFetchCallback callback) {
        Log.d("REZ_DB", "Query: " + query); // Log the query
        ArrayList<Product> products = new ArrayList<>();

        db.collection("products")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Product product = document.toObject(Product.class);
                            if (!product.isDeleted() && product.isVisibility() && product.getName().toLowerCase().contains(query.toLowerCase())) {
                                products.add(product);
                            }
                        }
                        // Invoke the callback with the filtered products list
                        callback.onProductFetch(products);
                    } else {
                        Log.w("REZ_DB", "Error getting documents.", task.getException());
                        // Invoke the callback with null if an error occurs
                        callback.onProductFetch(null);
                    }
                });
    }

    public void getById(ProductFetchCallback callback, String id) {
        ArrayList<Product> products = new ArrayList<>();

        db.collection("products").whereEqualTo("id", id)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Product product = document.toObject(Product.class);
                            if (!product.isDeleted()) {
                                products.add(product);
                            }
                        }
                        // Invoke the callback with the products list
                        callback.onProductFetch(products);
                    } else {
                        Log.w("REZ_DB", "Error getting documents.", task.getException());
                        // Invoke the callback with null if an error occurs
                        callback.onProductFetch(null);
                    }
                });
    }


    public interface ProductFetchCallback {
        default void onProductFetch(ArrayList<Product> products) {
        }
        default void onResult(boolean contains) {
        }
    }

    public static String generateId() {
        return "product_" + System.currentTimeMillis();
    }
}
