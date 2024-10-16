package com.barbershop.appointments.mapperlayer;

import com.barbershop.appointments.dataaccesslayer.Appointment;
import com.barbershop.appointments.dataaccesslayer.AppointmentIdentifier;
import com.barbershop.appointments.domainclientlayer.barbers.BarberModel;
import com.barbershop.appointments.domainclientlayer.clients.ClientModel;
import com.barbershop.appointments.domainclientlayer.reviews.ReviewModel;
import com.barbershop.appointments.presentationlayer.AppointmentRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AppointmentRequestMapper {

    @Mapping(target = "id", ignore = true)
    Appointment requestModelToEntity(AppointmentRequestModel appointmentRequestModel, AppointmentIdentifier
                                     appointmentIdentifier, BarberModel barberModel, ClientModel clientModel,
                                     ReviewModel reviewModel);
}
