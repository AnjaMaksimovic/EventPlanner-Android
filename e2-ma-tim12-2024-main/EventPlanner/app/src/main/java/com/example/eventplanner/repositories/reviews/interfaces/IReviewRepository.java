package com.example.eventplanner.repositories.reviews.interfaces;

import com.example.eventplanner.model.reviews.Review;

public interface IReviewRepository {
    public void Create(Review review);
    public void Delete(String reviewId);
    public void GetAllByCompany(String companyId, GetReviewsCallback callback);
}
