package com.barbershop.appointments.businesslayer;

import com.barbershop.appointments.dataaccesslayer.Appointment;
import com.barbershop.appointments.dataaccesslayer.AppointmentIdentifier;
import com.barbershop.appointments.dataaccesslayer.AppointmentRepository;
import com.barbershop.appointments.domainclientlayer.barbers.BarberModel;
import com.barbershop.appointments.domainclientlayer.barbers.BarberServiceClient;
import com.barbershop.appointments.domainclientlayer.clients.ClientModel;
import com.barbershop.appointments.domainclientlayer.clients.ClientServiceClient;
import com.barbershop.appointments.domainclientlayer.reviews.ReviewModel;
import com.barbershop.appointments.domainclientlayer.reviews.ReviewServiceClient;
import com.barbershop.appointments.mapperlayer.AppointmentRequestMapper;
import com.barbershop.appointments.mapperlayer.AppointmentResponseMapper;
import com.barbershop.appointments.presentationlayer.AppointmentRequestModel;
import com.barbershop.appointments.presentationlayer.AppointmentResponseModel;
import com.barbershop.appointments.utils.exceptions.InvalidInputException;
import com.barbershop.appointments.utils.exceptions.NoMoreThanFiveNumberOfPeopleException;
import com.barbershop.appointments.utils.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class AppointmentServiceImpl implements AppointmentService{

    private final AppointmentRepository appointmentRepository;
    private final AppointmentRequestMapper appointmentRequestMapper;
    private final AppointmentResponseMapper appointmentResponseMapper;

    private final BarberServiceClient barberServiceClient;
    private final ClientServiceClient clientServiceClient;
    private final ReviewServiceClient reviewServiceClient;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository,
                                  AppointmentRequestMapper appointmentRequestMapper,
                                  AppointmentResponseMapper appointmentResponseMapper, BarberServiceClient barberServiceClient, ClientServiceClient clientServiceClient, ReviewServiceClient reviewServiceClient) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentRequestMapper = appointmentRequestMapper;
        this.appointmentResponseMapper = appointmentResponseMapper;

        this.barberServiceClient = barberServiceClient;
        this.clientServiceClient = clientServiceClient;
        this.reviewServiceClient = reviewServiceClient;
    }

    @Override
    public List<AppointmentResponseModel> getAllAppointmentsForBarber(String barberId) {
        BarberModel foundBarber = barberServiceClient.getBarberByBarberId(barberId);

        if (foundBarber == null){
            throw new InvalidInputException("Barber ID not valid: " + barberId);
        }

        List<Appointment> barberAppointments = appointmentRepository.findAppointmentsByBarberModel_Sin(barberId);

        return appointmentResponseMapper.entityListToResponseModelList(barberAppointments);
    }

    @Override
    public AppointmentResponseModel getBarberAppointmentByAppointmentId(String barberId, String appointmentId) {
        BarberModel foundBarber = barberServiceClient.getBarberByBarberId(barberId);

        if (foundBarber == null){
            throw new InvalidInputException("Barber ID not valid: " + barberId);
        }

        Appointment foundAppointment = appointmentRepository.
                findAppointmentByBarberModel_SinAndAppointmentIdentifier_AppointmentId(barberId, appointmentId);
        if (foundAppointment == null){
            throw new NotFoundException("Appointment not found: " + appointmentId);
        }
        return appointmentResponseMapper.entityToResponseModel(foundAppointment);
    }

    @Override
    public AppointmentResponseModel addAppointmentToBarber(AppointmentRequestModel appointmentRequestModel,
                                                           String barberId) {
        BarberModel foundBarber = barberServiceClient.getBarberByBarberId(barberId);
        if (foundBarber == null){
            throw new InvalidInputException("Barber ID not valid: " + barberId);
        }

        ClientModel foundClient = clientServiceClient.getClientByClientId(appointmentRequestModel.getClientId());
        if (foundClient == null){
            throw new InvalidInputException("Client ID not valid: " + appointmentRequestModel.getClientId());
        }

        //cambiar este desgraciado por si se pudre el rancho
        ReviewModel foundReview = reviewServiceClient.getReviewByReviewId(appointmentRequestModel.getReviewId());
        if (foundReview == null){
            throw new InvalidInputException("Review ID not valid: " + appointmentRequestModel.getReviewId());
        }

        Appointment appointment = appointmentRequestMapper.requestModelToEntity(appointmentRequestModel,
                new AppointmentIdentifier(), foundBarber, foundClient, foundReview);

        if (appointmentRequestModel.getNumberOfPeople() > 5){
            throw new NoMoreThanFiveNumberOfPeopleException("Number of people exceeds 5" +
                    appointmentRequestModel.getNumberOfPeople());
        }

        Appointment savedAppointment = appointmentRepository.save(appointment);
        return appointmentResponseMapper.entityToResponseModel(savedAppointment);
    }

    @Override
    public AppointmentResponseModel updateAppointmentToBarber(AppointmentRequestModel appointmentRequestModel,
                                                              String barberId, String appointmentId) {
        BarberModel foundBarber = barberServiceClient.getBarberByBarberId(barberId);
        if (foundBarber == null){
            throw new InvalidInputException("Barber ID not valid: " + barberId);
        }

        Appointment foundAppointment = appointmentRepository.
                findAppointmentByBarberModel_SinAndAppointmentIdentifier_AppointmentId(barberId, appointmentId);
        if (foundAppointment == null){
            throw new NotFoundException("Appointment not found: " + appointmentId);
        }

        ClientModel foundClient = clientServiceClient.getClientByClientId(appointmentRequestModel.getClientId());
        if (foundClient == null){
            throw new InvalidInputException("Client ID not valid: " + appointmentRequestModel.getClientId());
        }

        //cambiar este desgraciado por si se pudre el rancho
        ReviewModel foundReview = reviewServiceClient.getReviewByReviewId(appointmentRequestModel.getReviewId());
        if (foundReview == null){
            throw new InvalidInputException("Review ID not valid: " + appointmentRequestModel.getReviewId());
        }

        Appointment updatedAppointment = appointmentRequestMapper.requestModelToEntity(appointmentRequestModel,
                foundAppointment.getAppointmentIdentifier(), foundBarber, foundClient, foundReview);

        //loop for players participating, go through players and update their stats
        //Invariant
       /* barberServiceClient.updateBarberAvailability(appointmentRequestModel.getBarberId(), appointmentRequestModel.getIsAvailable());*/

        updatedAppointment.setId(foundAppointment.getId());

        return appointmentResponseMapper.entityToResponseModel(appointmentRepository.save(updatedAppointment));
    }

    @Override
    public void deleteAppointmentToBarber(String barberId, String appointmentId) {
        BarberModel foundBarber = barberServiceClient.getBarberByBarberId(barberId);
        if (foundBarber == null){
            throw new InvalidInputException("Barber ID not valid: " + barberId);
        }

        Appointment foundAppointment = appointmentRepository.
                findAppointmentByBarberModel_SinAndAppointmentIdentifier_AppointmentId(barberId, appointmentId);
        if (foundAppointment == null){
            throw new NotFoundException("Appointment not found: " + appointmentId);
        }

        appointmentRepository.delete(foundAppointment);
    }
}
