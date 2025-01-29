package com.example.eventplanner.repositories.reviews.interfaces;

import com.example.eventplanner.model.reviews.Review;

import java.util.ArrayList;

public interface GetReviewsCallback {
    default void OnReviewsGet(ArrayList<Review> reviews){}
    default void OnResult(boolean contains){}
}
