package com.barbershop.reviews.presentationlayer;

import com.barbershop.reviews.businesslayer.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
/*@RequestMapping("api/v1/barbers/{barberId}/reviews")*/
@RequestMapping("api/v1/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping()
    public ResponseEntity<List<ReviewResponseModel>> getAllReviews(){
        return ResponseEntity.ok().body(reviewService.getAllReviews());
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseModel> getReviewByReviewId(@PathVariable String reviewId){
        return ResponseEntity.ok().body(reviewService.getReviewByReviewId(reviewId));
    }

    @PostMapping()
    public ResponseEntity<ReviewResponseModel> addReview(@RequestBody ReviewRequestModel reviewRequestModel){
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.addReview(reviewRequestModel));
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseModel> updateReview(@RequestBody ReviewRequestModel reviewRequestModel,
                                                                    @PathVariable String reviewId){
        return ResponseEntity.ok().body(reviewService.updateReview(reviewRequestModel, reviewId));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable String reviewId){
        reviewService.deleteReview(reviewId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //maybe to use after
    /*@GetMapping()
    public ResponseEntity<List<ReviewResponseModel>> getAllReviewsForBarber(@PathVariable String barberId){
        return ResponseEntity.ok().body(reviewService.getAllReviewsForBarber(barberId));
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseModel> getReviewByBarberId(@PathVariable String reviewId,
                                                                   @PathVariable String barberId){
        return ResponseEntity.ok().body(reviewService.getBarberReviewByReviewId(barberId, reviewId));
    }

    @PostMapping()
    public ResponseEntity<ReviewResponseModel> addReviewToBarber(@RequestBody ReviewRequestModel reviewRequestModel,
                                                                 @PathVariable String barberId){
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.addReviewToBarber(reviewRequestModel,
                barberId));
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseModel> updateReviewToBarber(@RequestBody ReviewRequestModel reviewRequestModel,
                                                            @PathVariable String reviewId, @PathVariable String barberId){
        return ResponseEntity.ok().body(reviewService.updateReviewToBarber(reviewRequestModel, barberId, reviewId));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReviewToBarber(@PathVariable String reviewId, @PathVariable String barberId){
        reviewService.deleteReviewToBarber(barberId, reviewId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }*/
}
