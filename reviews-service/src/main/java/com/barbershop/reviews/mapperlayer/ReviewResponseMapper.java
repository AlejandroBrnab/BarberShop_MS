package com.barbershop.reviews.mapperlayer;

import com.barbershop.reviews.dataaccesslayer.Review;
import com.barbershop.reviews.presentationlayer.ReviewResponseModel;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.hateoas.Link;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Mapper(componentModel = "spring")
public interface ReviewResponseMapper {


    @Mapping(expression = "java(review.getReviewIdentifier().getReviewId())", target = "reviewId")
    /*@Mapping(expression = "java(review.getBarberIdentifier().getSin())", target = "barberId")
    @Mapping(expression = "java(review.getClientIdentifier().getClientId())", target = "clientId")
    @Mapping(expression = "java(review.getAppointmentIdentifier().getAppointmentId())", target = "appointmentId")
    @Mapping(expression = "java(barber.getFirstName())", target = "barberFirstName")
    *//*@Mapping(expression = "java(barber.getFirstName())", target = "jUAN")*//*
    @Mapping(expression = "java(barber.getLastName())", target = "barberLastName")
    @Mapping(expression = "java(client.getFirstName())", target = "clientFirstName")
    @Mapping(expression = "java(client.getLastName())", target = "clientLastName")
    @Mapping(expression = "java(appointment.getNumberOfPeople())", target = "numberOfPeople")
    @Mapping(expression = "java(appointment.getServices())", target = "services")*/
    ReviewResponseModel entityToResponseModel(Review review/*, Barber barber, Client client, Appointment appointment*/);

    //cosas facil
    List<ReviewResponseModel> entityListToResponseModelList(List<Review> reviewList);

    /*@AfterMapping
    default void addLinks(@MappingTarget ReviewResponseModel model, Barber barber, Client client, Appointment appointment){
        //self link
        Link selfLink = linkTo(methodOn(ReviewController.class).getReviewByBarberId(model.getReviewId(),
                model.getBarberId())).withSelfRel();
        model.add(selfLink);

        //all reviews for barber
        Link reviewsLink = linkTo(methodOn(ReviewController.class).getAllReviewsForBarber(model.getBarberId())).
                withRel("all reviews");
        model.add(reviewsLink);

        //appointment link
        Link appointmentLink = linkTo(methodOn(AppointmentController.class).getBarberAppointmentByAppointmentId(
                barber.getBarberIdentifier().getSin(), appointment.getAppointmentIdentifier().getAppointmentId())).
                withRel("appointment");
        model.add(appointmentLink);
    }*/
}
