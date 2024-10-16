package com.barbershop.reviews.presentationlayer;

import com.barbershop.reviews.businesslayer.ReviewService;
import com.barbershop.reviews.utils.exceptions.InvalidInputException;
import com.barbershop.reviews.utils.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = ReviewController.class)
class ReviewControllerUnitTest {

    private final String FOUND_REVIEW_ID = "98-8359696";

    private final String NOT_FOUND_REVIEW_ID = "81-712-916";

    private final String INVALID_REVIEW_ID = "81-9962";

    @Autowired
    ReviewController reviewController;

    @MockBean
    private ReviewService reviewService;

    @Test
    public void whenNoReviewExists_thenReturnEmptyList(){
        //arrange
        when(reviewService.getAllReviews()).thenReturn(Collections.emptyList());

        //act
        ResponseEntity<List<ReviewResponseModel>> responseEntity= reviewController.getAllReviews();

        //assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().isEmpty());
        verify(reviewService, times(1)).getAllReviews();
    }

    @Test
    public void whenReviewExists_thenReturnReview(){
        //arrange

        ReviewRequestModel reviewRequestModel= ReviewRequestModel.builder().build();
        ReviewResponseModel reviewResponseModel= ReviewResponseModel.builder().build();

        when(reviewService.addReview(reviewRequestModel)).thenReturn(reviewResponseModel);

        //act
        ResponseEntity<ReviewResponseModel> responseEntity= reviewController.addReview(reviewRequestModel);

        //assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(reviewResponseModel, responseEntity.getBody());
        verify(reviewService, times(1)).addReview(reviewRequestModel);
    }

    @Test
    public void whenReviewExists_thenDeleteReview() throws NotFoundException {
        // Arrange
        doNothing().when(reviewService).deleteReview(FOUND_REVIEW_ID);

        // Act
        ResponseEntity<Void> responseEntity = reviewController.deleteReview(FOUND_REVIEW_ID);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(reviewService, times(1)).deleteReview(FOUND_REVIEW_ID);
    }

    @Test
    public void WhenReviewDoesNotExistOnDelete_thenReturnNotFoundException() throws NotFoundException {
        // Arrange
        String nonExistentReviewId = INVALID_REVIEW_ID;
        doThrow(NotFoundException.class).when(reviewService).deleteReview(nonExistentReviewId);

        // Act and Assert
        try {
            reviewController.deleteReview(nonExistentReviewId);
            fail("Expected NotFoundException");
        } catch (NotFoundException e) {
            // Verify that NotFoundException was thrown
            verify(reviewService, times(1)).deleteReview(nonExistentReviewId);
        }
    }


    @Test
    public void whenReviewNotFoundOnGet_thenReturnNotFoundException() {
        // Arrange
        when(reviewService.getReviewByReviewId(NOT_FOUND_REVIEW_ID)).thenThrow(NotFoundException.class);

        // Act and Assert
        try {
            reviewController.getReviewByReviewId(NOT_FOUND_REVIEW_ID);
            fail("Expected NotFoundException");
        } catch (NotFoundException e) {
            // Verify that NotFoundException was thrown
            verify(reviewService, times(1)).getReviewByReviewId(NOT_FOUND_REVIEW_ID);
        }
    }

    @Test
    public void whenReviewNotExistOnUpdate_thenReturnNotFoundException() throws NotFoundException {
        // Arrange
        ReviewRequestModel updatedReview = new ReviewRequestModel("mal loco", 0);
        when(reviewService.updateReview(updatedReview, NOT_FOUND_REVIEW_ID)).thenThrow(NotFoundException.class);

        // Act and Assert
        try {
            reviewController.updateReview(updatedReview, NOT_FOUND_REVIEW_ID);
            fail("Expected NotFoundException");
        } catch (NotFoundException e) {
            // Verify that NotFoundException was thrown
            verify(reviewService, times(1)).updateReview(updatedReview, NOT_FOUND_REVIEW_ID);
        }
    }


    @Test
    public void whenReviewExist_thenReturnUpdateReview() throws NotFoundException {
        // Arrange
        String existingReviewId = FOUND_REVIEW_ID;
        ReviewRequestModel updatedReview = new ReviewRequestModel("Mid!", 1);
        ReviewResponseModel updatedResponse = ReviewResponseModel.builder().reviewId(FOUND_REVIEW_ID).build();

        when(reviewService.updateReview(updatedReview, FOUND_REVIEW_ID)).thenReturn(updatedResponse);

        // Act
        ResponseEntity<ReviewResponseModel> responseEntity = reviewController.updateReview(updatedReview, FOUND_REVIEW_ID);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(FOUND_REVIEW_ID, responseEntity.getBody().getReviewId());
        verify(reviewService, times(1)).updateReview(updatedReview, FOUND_REVIEW_ID);
    }


    @Test
    public void whenCustomerNotFoundOnGet_ThenThrowNotFoundException() {
        //arrange
        when(reviewService.getReviewByReviewId(NOT_FOUND_REVIEW_ID)).thenThrow(new NotFoundException(
                "Unknown review ID: " + NOT_FOUND_REVIEW_ID));

        //act
        NotFoundException exception = assertThrowsExactly(NotFoundException.class, () -> {
            reviewController.getReviewByReviewId(NOT_FOUND_REVIEW_ID);
        });

        //assert
        assertEquals("Unknown review ID: " + NOT_FOUND_REVIEW_ID, exception.getMessage());
        verify(reviewService, times(1)).getReviewByReviewId(NOT_FOUND_REVIEW_ID);
    }

    @Test
    public void whenCustomerNotCorrectOnPost_ThenThrowNotValidInputException() {
        //arrange
        when(reviewService.getReviewByReviewId(INVALID_REVIEW_ID)).thenThrow(new InvalidInputException(
                "Not valid review ID: " + INVALID_REVIEW_ID));

        //act
        InvalidInputException exception = assertThrowsExactly(InvalidInputException.class, () -> {
            reviewController.getReviewByReviewId(INVALID_REVIEW_ID);
        });

        //assert
        assertEquals("Not valid review ID: " + INVALID_REVIEW_ID, exception.getMessage());
        verify(reviewService, times(1)).getReviewByReviewId(INVALID_REVIEW_ID);
    }
}