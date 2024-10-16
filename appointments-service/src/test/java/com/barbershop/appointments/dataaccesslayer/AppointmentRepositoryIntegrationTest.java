package com.barbershop.appointments.dataaccesslayer;

import com.barbershop.appointments.domainclientlayer.barbers.BarberModel;
import com.barbershop.appointments.domainclientlayer.clients.ClientModel;
import com.barbershop.appointments.domainclientlayer.reviews.ReviewModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ActiveProfiles("test")
class AppointmentRepositoryIntegrationTest {

    @Autowired
    AppointmentRepository appointmentRepository;

    @Test
    public void whenBarberIdIsValid_thenReturnAppointment(){

        var appointmentIdentifier1 = new AppointmentIdentifier();
        var barberModel = BarberModel.builder().sin("185-48-4455").firstName("John").lastName("Doe").isAvailable(true).build();

        var clientModel = ClientModel.builder().clientId("81-712-9162").firstName("Alice").lastName("Johnson").build();

        var reviewModel = ReviewModel.builder().description("Good").reviewId("98-8359696").score("9").build();

        ArrayList<Service> servicios = new ArrayList<>();
        Service service1 = new Service("hair");
        Service service2 = new Service("nails");
        servicios.add(service1);
        servicios.add(service2);

        var appointment1 = Appointment.builder().appointmentIdentifier(appointmentIdentifier1).barberModel(barberModel).
                clientModel(clientModel).reviewModel(reviewModel).services(servicios).
                date(LocalDate.of(2024,5,02)).time(LocalTime.of(14,30))
                .numberOfPeople(2).build();

        /*var appointment2 = Appointment.builder().appointmentIdentifier(new AppointmentIdentifier()).barberModel(barberModel).
                clientModel(clientModel).reviewModel(reviewModel).services(servicios).
                date(LocalDate.of(2024,5,02)).time(LocalTime.of(14,30))
                .numberOfPeople(2).build();*/

        appointmentRepository.save(appointment1);
        /*appointmentRepository.save(appointment2);*/

        List<Appointment> appointments = appointmentRepository.findAppointmentsByBarberModel_Sin(barberModel.getSin());

        //assert
        assertNotNull(appointments);
        assertEquals(2, appointments.size());

    }

    @Test
    public void whenBarberIdIsInvalid_thenReturnNull(){

        var appointmentIdentifier1 = new AppointmentIdentifier();
        var barberModel = BarberModel.builder().sin("185-48-4455").firstName("John").lastName("Doe").isAvailable(true).build();

        var clientModel = ClientModel.builder().clientId("81-712-9162").firstName("Alice").lastName("Johnson").build();

        var reviewModel = ReviewModel.builder().description("Good").reviewId("98-8359696").score("9").build();

        ArrayList<Service> servicios = new ArrayList<>();
        Service service1 = new Service("hair");
        Service service2 = new Service("nails");
        servicios.add(service1);
        servicios.add(service2);

        var appointment1 = Appointment.builder().appointmentIdentifier(appointmentIdentifier1).barberModel(barberModel).
                clientModel(clientModel).reviewModel(reviewModel).services(servicios).
                date(LocalDate.of(2024,5,02)).time(LocalTime.of(14,30))
                .numberOfPeople(2).build();

        appointmentRepository.save(appointment1);

        Appointment appointment = appointmentRepository.
                findAppointmentByBarberModel_SinAndAppointmentIdentifier_AppointmentId(barberModel.getSin(), "000");

        //assert
        assertNull(appointment);

    }
}