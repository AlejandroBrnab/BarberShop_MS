package com.barbershop.apigateway.mappinglayer.appointments;

import com.barbershop.apigateway.presentationlayer.appointments.AppointmentController;
import com.barbershop.apigateway.presentationlayer.appointments.AppointmentResponseModel;
import org.mapstruct.*;
import org.springframework.hateoas.Link;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface AppointmentResponseMapper {

    AppointmentResponseModel responseModelToResponseModel(AppointmentResponseModel appointmentResponseModel);

    List<AppointmentResponseModel> responseModelListToResponseModelList(List<AppointmentResponseModel>
                                                                                appointmentResponseModelList);

    @AfterMapping
    default void addLinks(@MappingTarget AppointmentResponseModel appointmentResponseModel){
        //customer link
        Link selfLink = linkTo(methodOn(AppointmentController.class).
                getBarberAppointmentByAppointmentId(appointmentResponseModel.getBarberId(),
                        appointmentResponseModel.getAppointmentId())).withSelfRel();
        appointmentResponseModel.add(selfLink);

        Link allLinks = linkTo(methodOn(AppointmentController.class).
                getAllAppointmentsForBarber(appointmentResponseModel.getBarberId())).withRel("all appointments");
        appointmentResponseModel.add(allLinks);
    }
}