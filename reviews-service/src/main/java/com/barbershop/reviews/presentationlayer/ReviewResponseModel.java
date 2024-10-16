package com.barbershop.reviews.presentationlayer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ReviewResponseModel /*extends RepresentationModel<ReviewResponseModel>*/ {

    private String reviewId;
    private String description;
    private Integer score;
}
