package com.barbershop.appointments.businesslayer;


import com.barbershop.appointments.presentationlayer.AppointmentRequestModel;
import com.barbershop.appointments.presentationlayer.AppointmentResponseModel;

import java.util.List;

public interface AppointmentService {

    public List<AppointmentResponseModel> getAllAppointmentsForBarber(String barberId);

    public AppointmentResponseModel getBarberAppointmentByAppointmentId(String barberId, String appointmentId);

    public AppointmentResponseModel addAppointmentToBarber(AppointmentRequestModel appointmentRequestModel,
                                                           String barberId);

    public AppointmentResponseModel updateAppointmentToBarber(AppointmentRequestModel appointmentRequestModel,
                                                              String barberId, String appointmentId);

    public void deleteAppointmentToBarber(String barberId, String appointmentId);

}
