package com.barbershop.appointments.presentationlayer;


import com.barbershop.appointments.dataaccesslayer.Service;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class AppointmentRequestModel extends RepresentationModel<AppointmentRequestModel> {

    private String barberId;
    private String clientId;
    private String reviewId; //borrar esto por si acaso NO SIRVE O CAMBIAR TO AUXILIO
    private List<Service> services;
    private LocalDate date;
    private LocalTime time;
    private Integer numberOfPeople;
    private Boolean isAvailable;
}
