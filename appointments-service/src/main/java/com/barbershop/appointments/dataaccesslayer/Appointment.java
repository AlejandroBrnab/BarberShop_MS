package com.barbershop.appointments.dataaccesslayer;

import com.barbershop.appointments.domainclientlayer.barbers.BarberModel;
import com.barbershop.appointments.domainclientlayer.clients.ClientModel;
import com.barbershop.appointments.domainclientlayer.reviews.ReviewModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Document(collection = "appointments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {

    @Id
    private String id;

    @Indexed(unique = true)
    private AppointmentIdentifier appointmentIdentifier;
    private BarberModel barberModel;
    private ClientModel clientModel;
    private ReviewModel reviewModel;
    private List<Service> services;
    private LocalDate date;
    private LocalTime time;
    private Integer numberOfPeople;

}
