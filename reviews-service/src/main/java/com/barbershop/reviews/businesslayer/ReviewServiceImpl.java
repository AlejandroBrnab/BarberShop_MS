package com.barbershop.reviews.businesslayer;

import com.barbershop.reviews.dataaccesslayer.Review;
import com.barbershop.reviews.dataaccesslayer.ReviewIdentifier;
import com.barbershop.reviews.dataaccesslayer.ReviewRepository;
import com.barbershop.reviews.mapperlayer.ReviewRequestMapper;
import com.barbershop.reviews.mapperlayer.ReviewResponseMapper;
import com.barbershop.reviews.presentationlayer.ReviewRequestModel;
import com.barbershop.reviews.presentationlayer.ReviewResponseModel;
import com.barbershop.reviews.utils.exceptions.InvalidScoreException;
import com.barbershop.reviews.utils.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;
    private final ReviewRequestMapper reviewRequestMapper;
    private final ReviewResponseMapper reviewResponseMapper;

    public ReviewServiceImpl(ReviewRepository reviewRepository, ReviewRequestMapper reviewRequestMapper,
                             ReviewResponseMapper reviewResponseMapper) {
        this.reviewRepository = reviewRepository;
        this.reviewRequestMapper = reviewRequestMapper;
        this.reviewResponseMapper = reviewResponseMapper;
    }

    //to make things easy
    @Override
    public List<ReviewResponseModel> getAllReviews() {
        List<Review> reviewList = reviewRepository.findAll();
        return reviewResponseMapper.entityListToResponseModelList(reviewList);
    }

    @Override
    public ReviewResponseModel getReviewByReviewId(String reviewId) {
        Review review = reviewRepository.findReviewByReviewIdentifier_ReviewId(reviewId);

        if (review == null){
            throw new NotFoundException("Unknown review ID: " + reviewId);
        }
        return reviewResponseMapper.entityToResponseModel(review);
    }

    @Override
    public ReviewResponseModel addReview(ReviewRequestModel reviewRequestModel) {
        Review review = reviewRequestMapper.requestModelToEntity(reviewRequestModel, new ReviewIdentifier());
        /*review.setDescription(reviewRequestModel.getDescription());
        review.setScore(reviewRequestModel.getScore());*/
        if (reviewRequestModel.getScore() > 10){
            throw new InvalidScoreException("Score must be less or equal than 10.");
        }
        return reviewResponseMapper.entityToResponseModel(reviewRepository.save(review));
    }

    @Override
    public ReviewResponseModel updateReview(ReviewRequestModel reviewRequestModel, String reviewId) {
        Review foundReview = reviewRepository.findReviewByReviewIdentifier_ReviewId(reviewId);
        if (foundReview == null){
            throw new NotFoundException("Unknown review ID: " + reviewId);
        }
        if (foundReview.getScore() > 10){
            throw new InvalidScoreException("Score must be less or equal than 10.");
        }
        Review review = reviewRequestMapper.requestModelToEntity(reviewRequestModel, foundReview.getReviewIdentifier());
        /*review.setDescription(reviewRequestModel.getDescription());
        review.setScore(reviewRequestModel.getScore());*/
        review.setId(foundReview.getId());
        return reviewResponseMapper.entityToResponseModel(reviewRepository.save(review));
    }

    @Override
    public void deleteReview(String reviewId) {
        Review foundReview = reviewRepository.findReviewByReviewIdentifier_ReviewId(reviewId);
        if (foundReview == null){
            throw new NotFoundException("Unknown review ID: " + reviewId);
        }
        reviewRepository.delete(foundReview);
    }
}
