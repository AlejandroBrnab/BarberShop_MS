package com.barbershop.reviews.dataaccesslayer;


import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.util.UUID;

@Embeddable
@Getter
public class ReviewIdentifier {

    private String reviewId;

    public ReviewIdentifier(String reviewId) {
        this.reviewId = reviewId;
    }

    public ReviewIdentifier() {
        this.reviewId = UUID.randomUUID().toString();
    }
}
