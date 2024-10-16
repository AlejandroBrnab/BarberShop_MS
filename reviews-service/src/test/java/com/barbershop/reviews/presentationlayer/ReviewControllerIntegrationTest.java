package com.barbershop.reviews.presentationlayer;

import com.barbershop.reviews.dataaccesslayer.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("h2")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ReviewControllerIntegrationTest {

    private final String BASE_URI_REVIEWS = "/api/v1/reviews";
    private final String FOUND_REVIEW_ID = "98-8359696";

    private final String FOUND_REVIEW_DESCRIPTION = "Mid!";

    private final Integer FOUND_REVIEW_SCORE = 1; //si no sirve esto es int

    private final String NOT_FOUND_REVIEW_ID = "81-712-916";

    private final String INVALID_REVIEW_ID = "81-9962";

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void whenGetReviews_thenReturnAllReviews() {
        //arrange
        long sizeDB = reviewRepository.count();
        //act
        webTestClient.get()
                .uri(BASE_URI_REVIEWS)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(ReviewResponseModel.class)
                .value((list) -> {
                    assertNotNull(list);
                    assertTrue(list.size() == sizeDB);
                    assertEquals(list.get(0).getReviewId(), FOUND_REVIEW_ID);
                });
    }

    @Test
    public void whenGetReviewDoesNotExist_thenReturnNotFound(){
        //act and assert
        webTestClient.get().uri(BASE_URI_REVIEWS + "/" + NOT_FOUND_REVIEW_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("Unknown review ID: " + NOT_FOUND_REVIEW_ID);
    }

    @Test
    public void whenValidReview_thenCreateReview() {
        //arrange
        long sizeDB = reviewRepository.count();

        ReviewRequestModel reviewRequestModel = new ReviewRequestModel("Miiid!", 0);

        webTestClient.post()
                .uri(BASE_URI_REVIEWS)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(reviewRequestModel)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ReviewResponseModel.class)
                .value((reviewResponseModel) -> {
                    assertNotNull(reviewResponseModel);
                    assertEquals(reviewRequestModel.getDescription(), reviewResponseModel.getDescription());
                    assertEquals(reviewRequestModel.getScore(), reviewResponseModel.getScore());
                });
        long sizeDbAfter = reviewRepository.count();
        assertEquals(sizeDB + 1, sizeDbAfter);
    }

    @Test
    public void whenReviewDoesNotExistOnUpdate_thenReturnNotFound() {
        // Arrange
        ReviewRequestModel reviewRequestModel = new ReviewRequestModel("malisimo", 0);

        // Perform a PUT or PATCH request to update a review that does not exist
        webTestClient.put()
                .uri(BASE_URI_REVIEWS + "/" + NOT_FOUND_REVIEW_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(reviewRequestModel) // Set the request body with the updated review data
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("Unknown review ID: " + NOT_FOUND_REVIEW_ID);
    }

    @Test
    public void whenReviewExists_thenDeleteReview() {
        // Arrange: Ensure that the review exists in the database and retrieve its ID
        String existingReviewId = FOUND_REVIEW_ID;

        // Act: Delete the review
        webTestClient.delete()
                .uri(BASE_URI_REVIEWS + "/" + FOUND_REVIEW_ID)
                .exchange()
                .expectStatus().isNoContent();

        // Assert: Verify the review is deleted
        webTestClient.get()
                .uri(BASE_URI_REVIEWS + "/" + FOUND_REVIEW_ID)
                .exchange().
                expectStatus().isNotFound();
    }

    @Test
    public void whenValidReview_thenUpdateReview() {
        // Arrange: Assuming "FOUND_REVIEW_ID" contains the ID of an existing review
        String reviewIdToUpdate = FOUND_REVIEW_ID;

        // Create the review update request model
        ReviewRequestModel reviewRequestModel = new ReviewRequestModel("Mid!", 1);

        // Perform the PUT request to update the review
        webTestClient.put()
                .uri(BASE_URI_REVIEWS + "/" + reviewIdToUpdate)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(reviewRequestModel)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ReviewResponseModel.class)
                .value((reviewResponseModel) -> {
                    assertNotNull(reviewResponseModel);
                    assertEquals(reviewRequestModel.getDescription(), reviewResponseModel.getDescription());
                    assertEquals(reviewRequestModel.getScore(), reviewResponseModel.getScore());

                });
    }

    @Test
    public void whenReviewExists_thenReturnReview() {
        // Arrange: Assuming there is a review with the specified ID in the database
        String existingReviewId = FOUND_REVIEW_ID;

        // Act: Perform a GET request to retrieve the review by ID
        webTestClient.get()
                .uri(BASE_URI_REVIEWS + "/" + FOUND_REVIEW_ID) // Adjust the URI to match your endpoint for retrieving reviews by ID
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ReviewResponseModel.class)
                .value((review) -> {
                    // Assert: Verify that the returned review is not null and has the correct properties
                    assertNotNull(review);
                    assertEquals("Mid!", review.getDescription());
                    assertEquals(1, review.getScore());
                });
    }

    @Test
    public void whenReviewsDontExistOnDelete_thenReturnNotFound() {
        // Arrange: Assuming there are no reviews in the database
        String nonExistingReviewId = INVALID_REVIEW_ID;

        // Act: Perform a DELETE request to delete a review that doesn't exist
        webTestClient.delete()
                .uri(BASE_URI_REVIEWS + "/" + nonExistingReviewId) // Adjust the URI to match your endpoint for deleting reviews by ID
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("Unknown review ID: " + nonExistingReviewId);
    }

    @Test
    public void whenScoreNotValidOnAdd_thenReturnUnprocessableEntity(){
        // Arrange
        String reviewId = FOUND_REVIEW_ID; // Replace with an existing league ID
        ReviewRequestModel reviewRequestModel = new ReviewRequestModel("Stop working", 100);

        // Act & Assert
        webTestClient.post()
                .uri(BASE_URI_REVIEWS)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(reviewRequestModel)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value())// Adjust this to 422, as Spring WebFlux may return 400 for custom exceptions by default
                .expectBody()
                .jsonPath("$.status").isEqualTo("UNPROCESSABLE_ENTITY") // Adjust the message according to your implementation
                .jsonPath("$.message").isEqualTo("Score must be less or equal than 10.");
    }

}