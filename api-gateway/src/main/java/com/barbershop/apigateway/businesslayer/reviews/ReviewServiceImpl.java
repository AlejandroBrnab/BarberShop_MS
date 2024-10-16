package com.barbershop.apigateway.businesslayer.reviews;

import com.barbershop.apigateway.domainserviceclient.reviews.ReviewServiceClient;
import com.barbershop.apigateway.mappinglayer.reviews.ReviewResponseMapper;
import com.barbershop.apigateway.presentationlayer.reviews.ReviewRequestModel;
import com.barbershop.apigateway.presentationlayer.reviews.ReviewResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ReviewServiceImpl implements ReviewService{

    private final ReviewServiceClient reviewServiceClient;
    private final ReviewResponseMapper reviewResponseMapper;

    public ReviewServiceImpl(ReviewServiceClient reviewServiceClient, ReviewResponseMapper reviewResponseMapper) {
        this.reviewServiceClient = reviewServiceClient;
        this.reviewResponseMapper = reviewResponseMapper;
    }

    @Override
    public List<ReviewResponseModel> getAllReviews() {
        return reviewResponseMapper.responseModelListToResponseModelList(reviewServiceClient.getAllReviews());
    }

    @Override
    public ReviewResponseModel getReviewByReviewId(String reviewId) {
        return reviewResponseMapper.responseModelToResponseModel(reviewServiceClient.getReviewByReviewId(reviewId));
    }

    @Override
    public ReviewResponseModel createReview(ReviewRequestModel review) {
        return reviewResponseMapper.responseModelToResponseModel(reviewServiceClient.addReview(review));
    }

    @Override
    public ReviewResponseModel updateReview(ReviewRequestModel review, String reviewId) {
        return reviewResponseMapper.responseModelToResponseModel(reviewServiceClient.updateReview(review, reviewId));
    }

    @Override
    public void deleteReview(String reviewId) {
        // Delegate the deletion operation to the BarberServiceClient
        reviewServiceClient.deleteReview(reviewId);
    }
}
