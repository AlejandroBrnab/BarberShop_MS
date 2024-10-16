package com.barbershop.appointments.utils;

import com.barbershop.appointments.dataaccesslayer.Appointment;
import com.barbershop.appointments.dataaccesslayer.AppointmentIdentifier;
import com.barbershop.appointments.dataaccesslayer.AppointmentRepository;
import com.barbershop.appointments.dataaccesslayer.Service;
import com.barbershop.appointments.domainclientlayer.barbers.BarberModel;
import com.barbershop.appointments.domainclientlayer.clients.ClientModel;
import com.barbershop.appointments.domainclientlayer.reviews.ReviewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

@Component
public class DatabaseLoaderServer implements CommandLineRunner {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Override
    public void run(String... args) throws Exception {
        var appointmentIdentifier1 = new AppointmentIdentifier();
        var appointmentIdentifier2 = new AppointmentIdentifier();

        ArrayList<Service> servicios = new ArrayList<>();
        Service service1 = new Service("hair");
        Service service2 = new Service("nails");
        servicios.add(service1);
        servicios.add(service2);

        var barberModel = BarberModel.builder().sin("185-48-4455").firstName("John").lastName("Doe").isAvailable(true).build();

        //comentar
        var barberModel2 = BarberModel.builder().sin("575-03-7490").firstName("Jane").lastName("Smith").isAvailable(true).build();

        var clientModel = ClientModel.builder().clientId("81-712-9162").firstName("Alice").lastName("Johnson").build();

        var reviewModel = ReviewModel.builder().description("Good").reviewId("98-8359696").score("9").build();

        var appointment = Appointment.builder().appointmentIdentifier(appointmentIdentifier1).barberModel(barberModel).
                clientModel(clientModel).reviewModel(reviewModel).services(servicios).
                date(LocalDate.of(2024,5,02)).time(LocalTime.of(14,30))
                .numberOfPeople(2).build();

        //comentar
        var appointment2 = Appointment.builder().appointmentIdentifier(appointmentIdentifier2).barberModel(barberModel2).
                clientModel(clientModel).reviewModel(reviewModel).services(servicios).
                date(LocalDate.of(2024,5,02)).time(LocalTime.of(14,30))
                .numberOfPeople(2).build();


        appointmentRepository.save(appointment);
        //comentar
        appointmentRepository.save(appointment2);
    }
}
