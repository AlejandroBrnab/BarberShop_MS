package com.barbershop.reviews.businesslayer;


import com.barbershop.reviews.presentationlayer.ReviewRequestModel;
import com.barbershop.reviews.presentationlayer.ReviewResponseModel;

import java.util.List;

public interface ReviewService {

    //pa hacer esto facil
    public List<ReviewResponseModel> getAllReviews();

    public ReviewResponseModel getReviewByReviewId(String reviewId);

    public ReviewResponseModel addReview(ReviewRequestModel reviewRequestModel);

    public ReviewResponseModel updateReview(ReviewRequestModel reviewRequestModel, String reviewId);

    public void deleteReview(String reviewId);
}
