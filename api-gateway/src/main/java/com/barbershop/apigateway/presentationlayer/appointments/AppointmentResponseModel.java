package com.barbershop.apigateway.presentationlayer.appointments;

import com.barbershop.apigateway.domainserviceclient.appointments.Service;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AppointmentResponseModel extends RepresentationModel<AppointmentResponseModel> {

    private String appointmentId;
    private String barberId;
    private String barberFirstName;
    private String barberLastName;
    private String clientId;
    private String clientFirstName;
    private String clientLastName;
    private List<Service> services;
    private LocalDate date;
    private LocalTime time;
    private Integer numberOfPeople;
    private String reviewId;
    private String description;
    private String score;
    private Boolean isAvailable;
}
