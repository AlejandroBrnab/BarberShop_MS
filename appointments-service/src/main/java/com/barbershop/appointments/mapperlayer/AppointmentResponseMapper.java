package com.barbershop.appointments.mapperlayer;

import com.barbershop.appointments.dataaccesslayer.Appointment;
import com.barbershop.appointments.presentationlayer.AppointmentResponseModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AppointmentResponseMapper {

    @Mapping(expression = "java(appointment.getAppointmentIdentifier().getAppointmentId())", target = "appointmentId")
    @Mapping(expression = "java(appointment.getBarberModel().getSin())", target = "barberId") //si no sirve poner sin
    @Mapping(expression = "java(appointment.getBarberModel().getIsAvailable())", target = "isAvailable") //POTENTIAL ERROR
    @Mapping(expression = "java(appointment.getClientModel().getClientId())", target = "clientId")
    @Mapping(expression = "java(appointment.getBarberModel().getFirstName())", target = "barberFirstName")
    @Mapping(expression = "java(appointment.getBarberModel().getLastName())", target = "barberLastName")
    @Mapping(expression = "java(appointment.getClientModel().getFirstName())", target = "clientFirstName")
    @Mapping(expression = "java(appointment.getClientModel().getLastName())", target = "clientLastName")
    @Mapping(expression = "java(appointment.getReviewModel().getReviewId())", target = "reviewId")
    @Mapping(expression = "java(appointment.getReviewModel().getScore())", target = "score")
    @Mapping(expression = "java(appointment.getReviewModel().getDescription())", target = "description")
    AppointmentResponseModel entityToResponseModel(Appointment appointment); //agregar services en el caso que no sirva

    List<AppointmentResponseModel> entityListToResponseModelList(List<Appointment> appointments);

}