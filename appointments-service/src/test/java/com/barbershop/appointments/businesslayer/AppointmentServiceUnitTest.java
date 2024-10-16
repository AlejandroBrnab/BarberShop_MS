package com.barbershop.appointments.businesslayer;

import com.barbershop.appointments.dataaccesslayer.Appointment;
import com.barbershop.appointments.dataaccesslayer.AppointmentIdentifier;
import com.barbershop.appointments.dataaccesslayer.AppointmentRepository;
import com.barbershop.appointments.dataaccesslayer.Service;
import com.barbershop.appointments.domainclientlayer.barbers.BarberModel;
import com.barbershop.appointments.domainclientlayer.barbers.BarberServiceClient;
import com.barbershop.appointments.domainclientlayer.clients.ClientModel;
import com.barbershop.appointments.domainclientlayer.clients.ClientServiceClient;
import com.barbershop.appointments.domainclientlayer.reviews.ReviewModel;
import com.barbershop.appointments.domainclientlayer.reviews.ReviewServiceClient;
import com.barbershop.appointments.mapperlayer.AppointmentResponseMapper;
import com.barbershop.appointments.presentationlayer.AppointmentRequestModel;
import com.barbershop.appointments.presentationlayer.AppointmentResponseModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration")
@ActiveProfiles("test")
class AppointmentServiceUnitTest {

    @Autowired
    AppointmentService appointmentService;

    @MockBean
    ClientServiceClient clientServiceClient;

    @MockBean
    BarberServiceClient barberServiceClient;

    @MockBean
    ReviewServiceClient reviewServiceClient;

    @MockBean
    AppointmentRepository appointmentRepository;

    @SpyBean
    AppointmentResponseMapper appointmentResponseMapper;


    @Test
    public void whenValidClientId_BarberId_ReviewId_thenProcessAppointment() {
        //arrange
        var clientModel = ClientModel.builder()
                .clientId("81-712-9162").firstName("Alice").lastName("Johnson").build();

        var barberModel = BarberModel.builder().sin("185-48-4455").firstName("John").lastName("Doe").isAvailable(true).build();

        var reviewModel = ReviewModel.builder().description("Good").reviewId("98-8359696").score("9").build();


        ArrayList<Service> servicios = new ArrayList<>();
        Service service1 = new Service("hair");
        Service service2 = new Service("nails");
        servicios.add(service1);
        servicios.add(service2);


        var appointmentRequestModel = AppointmentRequestModel.builder()
                .barberId("185-48-4455").clientId("81-712-9162").reviewId("98-8359696").services(servicios).
                date(LocalDate.of(2024,5,02)).time(LocalTime.of(14,30))
                .numberOfPeople(2).build();

        //appointment objects
        var appointmentIdentifier1 = new AppointmentIdentifier();
        var appointment1 = Appointment.builder().appointmentIdentifier(appointmentIdentifier1).barberModel(barberModel).
                clientModel(clientModel).reviewModel(reviewModel).services(servicios).
                date(LocalDate.of(2024,5,02)).time(LocalTime.of(14,30))
                .numberOfPeople(2).id("001").build();

        var updatedAppointment = Appointment.builder().appointmentIdentifier(appointmentIdentifier1).barberModel(barberModel).
                clientModel(clientModel).reviewModel(reviewModel).services(servicios).
                date(LocalDate.of(2024,5,02)).time(LocalTime.of(14,30))
                .numberOfPeople(2).id("001").build();

        //define mock behaviors

        when(clientServiceClient.getClientByClientId(clientModel.getClientId())).thenReturn(clientModel);

        when(barberServiceClient.getBarberByBarberId(barberModel.getSin())).thenReturn(barberModel);

        when(reviewServiceClient.getReviewByReviewId(reviewModel.getReviewId())).thenReturn(reviewModel);

        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment1, updatedAppointment); //first call returns appointment1, second class returns updatedSale

        //act
        AppointmentResponseModel appointmentResponseModel = appointmentService.addAppointmentToBarber(
                appointmentRequestModel, barberModel.getSin());

        //assert
        assertNotNull(appointmentResponseModel);
        assertEquals(clientModel.getClientId(), appointmentResponseModel.getClientId());
        assertEquals(clientModel.getFirstName(), appointmentResponseModel.getClientFirstName());
        assertEquals(clientModel.getLastName(), appointmentResponseModel.getClientLastName());
        assertEquals(barberModel.getSin(), appointmentResponseModel.getBarberId());
        assertEquals(barberModel.getFirstName(), appointmentResponseModel.getBarberFirstName());
        assertEquals(barberModel.getLastName(), appointmentResponseModel.getBarberLastName());
        assertEquals(reviewModel.getReviewId(), appointmentResponseModel.getReviewId());
        assertEquals(reviewModel.getDescription(), appointmentResponseModel.getDescription());
        assertEquals(appointmentRequestModel.getDate(), appointmentResponseModel.getDate());
        assertEquals(appointmentRequestModel.getTime(), appointmentResponseModel.getTime());
        assertEquals(appointmentRequestModel.getNumberOfPeople(), appointmentResponseModel.getNumberOfPeople());

        //for the spy
        verify(appointmentResponseMapper, times(1)).entityToResponseModel(updatedAppointment);
    }

}