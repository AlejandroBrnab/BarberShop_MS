package com.barbershop.apigateway.presentationlayer.reviews;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
//@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ReviewRequestModel {

    //en custodia
private String barberId;


private String clientId;
    private String appointmentId;

    private String description;
    private Integer score;
}
