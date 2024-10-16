package com.barbershop.apigateway.businesslayer.appointments;

import com.barbershop.apigateway.domainserviceclient.appointments.AppointmentServiceClient;
import com.barbershop.apigateway.mappinglayer.appointments.AppointmentResponseMapper;
import com.barbershop.apigateway.presentationlayer.appointments.AppointmentRequestModel;
import com.barbershop.apigateway.presentationlayer.appointments.AppointmentResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentServiceClient appointmentServiceClient;
    private final AppointmentResponseMapper appointmentResponseMapper;

    public AppointmentServiceImpl(AppointmentServiceClient appointmentServiceClient,
                                  AppointmentResponseMapper appointmentResponseMapper) {
        this.appointmentServiceClient = appointmentServiceClient;
        this.appointmentResponseMapper = appointmentResponseMapper;
    }

    @Override
    public List<AppointmentResponseModel> getAllAppointmentsForBarber(String barberId) {
        return appointmentResponseMapper.
                responseModelListToResponseModelList(appointmentServiceClient.getAllAppointments(barberId));
    }

    @Override
    public AppointmentResponseModel getBarberAppointmentByAppointmentId(String barberId, String appointmentId) {
        return appointmentResponseMapper.
                responseModelToResponseModel(appointmentServiceClient.
                        getAppointmentByBarberIdAndAppointmentId(barberId, appointmentId));
    }

    @Override
    public AppointmentResponseModel addAppointmentToBarber(AppointmentRequestModel appointmentRequestModel,
                                                           String barberId) {
        return appointmentResponseMapper.responseModelToResponseModel(appointmentServiceClient.
                addAppointment(appointmentRequestModel, barberId));
    }

    @Override
    public AppointmentResponseModel updateAppointmentToBarber(AppointmentRequestModel appointmentRequestModel,
                                                              String barberId, String appointmentId) {
        return appointmentResponseMapper.responseModelToResponseModel(appointmentServiceClient.updateAppointment(
                appointmentId, barberId, appointmentRequestModel));
    }

    @Override
    public void deleteAppointmentToBarber(String barberId, String appointmentId) {
        appointmentServiceClient.deleteAppointment(barberId, appointmentId);
    }
}
