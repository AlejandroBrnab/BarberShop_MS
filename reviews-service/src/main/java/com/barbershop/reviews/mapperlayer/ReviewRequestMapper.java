package com.barbershop.reviews.mapperlayer;

import com.barbershop.reviews.dataaccesslayer.Review;
import com.barbershop.reviews.dataaccesslayer.ReviewIdentifier;
import com.barbershop.reviews.presentationlayer.ReviewRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewRequestMapper {

    @Mapping(target = "id", ignore = true)
    /*@Mapping(expression = "java(reviewRequestModel.getDescription())", target = "description")
    @Mapping(expression = "java(reviewRequestModel.getScore())", target = "score")*/
    Review requestModelToEntity(ReviewRequestModel reviewRequestModel, ReviewIdentifier reviewIdentifier/*,
                                BarberIdentifier barberIdentifier, ClientIdentifier clientIdentifier,
                                AppointmentIdentifier appointmentIdentifier*/);
}
