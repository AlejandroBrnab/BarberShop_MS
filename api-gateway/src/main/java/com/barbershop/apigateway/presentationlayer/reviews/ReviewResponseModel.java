package com.barbershop.apigateway.presentationlayer.reviews;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ReviewResponseModel extends RepresentationModel<ReviewResponseModel> {

    private String reviewId;
    private String description;
    private Integer score;
}
