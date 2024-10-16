package com.barbershop.apigateway.businesslayer.reviews;

import com.barbershop.apigateway.presentationlayer.reviews.ReviewRequestModel;
import com.barbershop.apigateway.presentationlayer.reviews.ReviewResponseModel;

import java.util.List;

public interface ReviewService {

    List<ReviewResponseModel> getAllReviews();

    ReviewResponseModel getReviewByReviewId(String reviewId);

    ReviewResponseModel createReview(ReviewRequestModel review);

    ReviewResponseModel updateReview(ReviewRequestModel review, String reviewId);

    void deleteReview(String reviewId);
}
