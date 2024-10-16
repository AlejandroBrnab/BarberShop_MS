package com.barbershop.appointments.domainclientlayer.reviews;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
public class ReviewModel {

    String reviewId;
    String description;
    String score;
}
