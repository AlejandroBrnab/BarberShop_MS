package com.barbershop.apigateway.businesslayer.appointments;


import com.barbershop.apigateway.presentationlayer.appointments.AppointmentRequestModel;
import com.barbershop.apigateway.presentationlayer.appointments.AppointmentResponseModel;

import java.util.List;

public interface AppointmentService {

    List<AppointmentResponseModel> getAllAppointmentsForBarber(String barberId);

    AppointmentResponseModel getBarberAppointmentByAppointmentId(String barberId, String appointmentId);

    AppointmentResponseModel addAppointmentToBarber(AppointmentRequestModel appointmentRequestModel,
                                                           String barberId);

    AppointmentResponseModel updateAppointmentToBarber(AppointmentRequestModel appointmentRequestModel,
                                                              String barberId, String appointmentId);

    void deleteAppointmentToBarber(String barberId, String appointmentId);

}
