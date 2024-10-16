package com.barbershop.apigateway.presentationlayer.reviews;

import com.barbershop.apigateway.businesslayer.reviews.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReviewResponseModel>> getAllReviews(){
        return ResponseEntity.ok().body(reviewService.getAllReviews());
    }

    @GetMapping(value = "/{reviewId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReviewResponseModel> getReviewById(@PathVariable String reviewId){
        return ResponseEntity.ok().body(reviewService.getReviewByReviewId(reviewId));
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<ReviewResponseModel> addReview(@RequestBody ReviewRequestModel reviewRequestModel){
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.createReview(reviewRequestModel));
    }

    @PutMapping(value = "{reviewId}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<ReviewResponseModel> updateReview(@RequestBody ReviewRequestModel reviewRequestModel,
                                                            @PathVariable String reviewId){
        return ResponseEntity.ok().body(reviewService.updateReview(reviewRequestModel, reviewId));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void>deleteReview(@PathVariable String reviewId){
        reviewService.deleteReview(reviewId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
