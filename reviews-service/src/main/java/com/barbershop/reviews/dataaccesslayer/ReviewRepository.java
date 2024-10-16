package com.barbershop.reviews.dataaccesslayer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    /*Review findReviewByBarberIdentifier_SinAndReviewIdentifier_ReviewId(String barberId, String reviewId);

    List<Review> findReviewsByBarberIdentifier_Sin(String barberId);*/

    //pa hacerlo facil
    Review findReviewByReviewIdentifier_ReviewId(String reviewId);
}
