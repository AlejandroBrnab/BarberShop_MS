package com.barbershop.reviews.presentationlayer;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
//@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ReviewRequestModel {

    //en custodia
    /*private String barberId;*/

    /*private String clientId;
    private String appointmentId;*/
    private String description;
    private Integer score;
}
