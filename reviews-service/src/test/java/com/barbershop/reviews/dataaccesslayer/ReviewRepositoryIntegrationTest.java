package com.barbershop.reviews.dataaccesslayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ReviewRepositoryIntegrationTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @BeforeEach
    public void setUpDb(){
        reviewRepository.deleteAll();
    }

    @Test
    public void whenReviewExists_ReturnReviewByReviewId(){
        //arrange (set up test data)
        Review review1 = new Review("Naisu", 9);

        reviewRepository.save(review1);
        //act
        Review review =
                reviewRepository.findReviewByReviewIdentifier_ReviewId(review1.getReviewIdentifier().getReviewId());

        //assert
        assertNotNull(review);
        assertEquals(review.getReviewIdentifier(), review1.getReviewIdentifier());
        assertEquals(review.getDescription(), review1.getDescription());
        assertEquals(review.getScore(), review1.getScore());
    }

    @Test
    public void whenReviewDoesNotExist_ReturnNull(){
        Review savedReview= reviewRepository.findReviewByReviewIdentifier_ReviewId("1234");

        assertNull(savedReview);
    }

}